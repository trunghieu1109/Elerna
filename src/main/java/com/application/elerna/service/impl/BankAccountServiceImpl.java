package com.application.elerna.service.impl;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.BankAccountLogResponse;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.exception.InvalidRequestData;
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
     * @param request SignUpRequest
     * @param user User
     * @return BankAccount
     */
    @Override
    public BankAccount createBankAccount(SignUpRequest request, User user) {

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
            throw new ResourceNotFound("Course not found, id: " + request.getCourseId());
        }

        Course currentCourse = course.get();

        // check if account has enough money
        if (!checkRemainingAmount(currentCourse.getPrice(), user)) {

            throw new InvalidRequestData("Remaining account was enough to pay for this course, amount: " + user.getBankAccount().getAmount() + " but price is " + course.get().getPrice());
        }

        // implement payment
        BankAccount bankAccount = user.getBankAccount();

        bankAccount.setAmount(bankAccount.getAmount() - currentCourse.getPrice());

        if (bankAccount.getAmount() < BankAccount.MAINTAINING_AMOUNT) {
            return false;
        } else {

            bankAccountRepository.save(bankAccount);

            BankAccountLog bankAccountLog = BankAccountLog.builder()
                    .bankAccount(bankAccount)
                    .message("Account " + bankAccount.getId() + " pays for course " + request.getCourseId() + " successfully, amount = "
                            + course.get().getPrice() + ", residual = " + bankAccount.getAmount())
                    .messageType("Payment")
                    .build();

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
        User user = userService.getUserFromAuthentication();

        BankAccount bankAccount = user.getBankAccount();

        List<BankAccountLog> logs = bankAccount.getBankAccountLogs().stream().toList();

//        logs.sort(new Comparator<BankAccountLog>() {
//            @Override
//            public int compare(BankAccountLog o1, BankAccountLog o2) {
//                return o1.getId().compareTo(o2.getId());
//            }
//        });

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
            throw new InvalidRequestData("User is not matched with any bank accounts");
        }

        bankAccount.setAmount(bankAccount.getAmount() + amount);

        bankAccountRepository.save(bankAccount);

        BankAccountLog bankAccountLog = BankAccountLog.builder()
                .bankAccount(bankAccount)
                .messageType("Deposit")
                .message("Deposit money to bank account " + bankAccount.getId() + ", amount = " + amount + ", residual = " + bankAccount.getAmount())
                .build();

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
}
