package com.application.elerna.service.impl;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.BankAccountLogResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.exception.InvalidAccountRemaining;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.BankAccount;
import com.application.elerna.model.BankAccountLog;
import com.application.elerna.model.Course;
import com.application.elerna.model.User;
import com.application.elerna.repository.BankAccountLogRepository;
import com.application.elerna.repository.BankAccountRepository;
import com.application.elerna.repository.CourseRepository;
import com.application.elerna.service.BankAccountService;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final BankAccountLogRepository bankAccountLogRepository;

    /**
     *
     * Create bank account
     *
     * @param user User
     * @return BankAccount
     */
    @Override
    public BankAccount createBankAccount(User user) {

        return BankAccount.builder()
                .user(user)
                .cardHolder(user.getFirstName() + " " + user.getLastName())
                .cardNumber(user.getCardNumber())
                .amount(100.0)
                .bankAccountLogs(new HashSet<>())
                .build();
    }

    /**
     *
     * Check whether remaining account has enough
     * money to pay for course
     *
     * @param price Double
     * @param user User
     * @return boolean
     */
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    private boolean checkRemainingAmount(Double price, User user) {

        double remainingAccount = user.getBankAccount().getAmount();

        return remainingAccount >= price + BankAccount.MAINTAINING_AMOUNT;

    }

    /**
     *
     * Pay for course
     *
     * @param request PaymentRequest
     * @return boolean
     */
    @Override
    public boolean pay(PaymentRequest request) {

        // get user
        User user = userService.getUserFromAuthentication();

        System.out.println(user.getBankAccount().getCardHolder());

        // get course
        var course = courseRepository.findById(request.getCourseId());

        if (course.isEmpty()) {
            throw new ResourceNotFound("Course",  "courseId: " + request.getCourseId());
        }

        log.info("Course found, ready to pay for course");

        Course currentCourse = course.get();

        // check if account has enough money
        if (!checkRemainingAmount(currentCourse.getPrice(), user)) {

            throw new InvalidAccountRemaining("amount: " + user.getBankAccount().getAmount() + " but price is " + course.get().getPrice());
        }

        // implement payment
        BankAccount bankAccount = user.getBankAccount();

        log.info("Deducting money from account to pay for course, amount: {}, price: {}", bankAccount.getAmount(), currentCourse.getPrice(), bankAccount.getAmount() - currentCourse.getPrice());
        bankAccount.setAmount(bankAccount.getAmount() - currentCourse.getPrice());

        if (bankAccount.getAmount() < BankAccount.MAINTAINING_AMOUNT) {
            return false;
        } else {

            bankAccountRepository.save(bankAccount);

            log.info("Create new bank account logs");

            BankAccountLog bankAccountLog = BankAccountLog.builder()
                    .bankAccount(bankAccount)
                    .message("Account " + bankAccount.getId() + " pays for course " + request.getCourseId() + " successfully, amount = "
                            + course.get().getPrice() + ", residual = " + bankAccount.getAmount())
                    .messageType("Payment")
                    .build();

            log.info("Log message: {}", bankAccountLog.getMessage());

            bankAccountLogRepository.save(bankAccountLog);

            bankAccount.addBankAccountLog(bankAccountLog);

            bankAccountRepository.save(bankAccount);

            return true;
        }
    }

    /**
     *
     * Get bank account logs
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getBankAccountLogs(Integer pageNo, Integer pageSize) {

        log.info("Get bank account logs: pageNo: {}, pageSize: {}", pageNo, pageSize);

        User user = userService.getUserFromAuthentication();

        BankAccount bankAccount = user.getBankAccount();

        List<BankAccountLog> logs = bankAccount.getBankAccountLogs().stream().toList();

        int totalPages = (int) Math.ceil(logs.size() * 1.0 / pageSize);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .data(logs.subList(Math.max(pageNo * pageSize, 0), Math.min((pageNo + 1) * pageSize, logs.size())).stream().map(this::createBankLogsResponse))
                .build();

    }

    /**
     *
     * Deposit money to bank account
     *
     * @param amount Double
     * @return String
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String deposit(Double amount) {

        User user = userService.getUserFromAuthentication();

        BankAccount bankAccount = user.getBankAccount();

        if (bankAccount == null) {
            throw new ResourceNotFound("BankAccount", "cardNumber: " + user.getCardNumber());
        }
        log.info("Deposit {}$ for user {} with cardNumber: {}", amount, user.getUsername(), bankAccount.getCardNumber());


        bankAccount.setAmount(bankAccount.getAmount() + amount);

        bankAccountRepository.save(bankAccount);

        log.info("Create new bank logs for depositing");

        BankAccountLog bankAccountLog = BankAccountLog.builder()
                .bankAccount(bankAccount)
                .messageType("Deposit")
                .message("Deposit money to bank account " + bankAccount.getId() + ", amount = " + amount + ", residual = " + bankAccount.getAmount())
                .build();

        log.info("Log message: {}", bankAccountLog.getMessage());

        bankAccountLogRepository.save(bankAccountLog);
        bankAccount.addBankAccountLog(bankAccountLog);

        bankAccountRepository.save(bankAccount);

        return bankAccountLog.getMessage();
    }

    /**
     *
     * Create bank log response from bank account log
     *
     * @param logs BankAccountLogs
     * @return BankAccountLogResponse
     */
    private BankAccountLogResponse createBankLogsResponse(BankAccountLog logs) {
        return BankAccountLogResponse.builder()
                .createdAt(logs.getCreatedAt())
                .updatedAt(logs.getUpdatedAt())
                .message(logs.getMessage())
                .messageType(logs.getMessageType())
                .messageId(logs.getId())
                .build();
    }

    /**
     *
     * Save bank account to database
     *
     * @param account BankAccount
     */
    @Override
    public void saveBankAccount(BankAccount account) {
        bankAccountRepository.save(account);
    }

    @Override
    public BankAccount getByCardnumber(String cardNumber) {
        var bankAccount = bankAccountRepository.findByCardNumber(cardNumber);

        return bankAccount.orElse(null);

    }
}
