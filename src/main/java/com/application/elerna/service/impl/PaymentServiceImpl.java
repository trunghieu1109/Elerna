package com.application.elerna.service.impl;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TransactionResponse;
import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.*;
import com.application.elerna.repository.CourseRepository;
import com.application.elerna.repository.TransactionRepository;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PrivilegeService privilegeService;
    private final BankAccountService bankAccountService;

    private final RoleService roleService;

    /**
     *
     * Send payment requests
     *
     * @param request PaymentRequest
     * @return String
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String pay(PaymentRequest request) {

        // check user's bank account

        boolean isSuccessfully = bankAccountService.pay(request);

        // suppose that it successfully

        if (!isSuccessfully) {
            throw new InvalidRequestData("Cant pay for this course");
        }

        var course = courseRepository.findById(request.getCourseId());

        if (course.isEmpty() || !course.get().isStatus()) {

            throw new ResourceNotFound("Course is not ready, id = " + request.getCourseId());

        }

        Course currentCourse = course.get();

        User user = userService.getUserFromAuthentication();

        // create transaction
        Transaction transaction = Transaction.builder()
                .course(currentCourse)
                .user(user)
                .description(request.getDescription())
                .paymentMethod(request.getPaymentMethod())
                .price(course.get().getPrice())
                .status("done")
                .cardNumber(user.getCardNumber())
                .build();

        transactionRepository.save(transaction);

        currentCourse.addTransaction(transaction);
        user.addTransaction(transaction);

        courseRepository.save(currentCourse);
        userRepository.save(user);

        List<Transaction> transactions = transactionRepository.findAll();

        transactions.sort((t1, t2) -> t1.getId().compareTo(t2.getId()));

        Long lastId = transactions.get(transactions.size() - 1).getId();

        Privilege priView = privilegeService.createPrivilege("transaction", lastId, "view", "View transaction " + lastId);
        Privilege priDelete = privilegeService.createPrivilege("transaction", lastId, "delete", "Delete transaction " + lastId);

        Role roleAdmin = roleService.createRole("admin", "transaction", lastId);

        priView.addRole(roleAdmin);
        priDelete.addRole(roleAdmin);

        roleAdmin.addPrivilege(priDelete);
        roleAdmin.addPrivilege(priView);

        privilegeService.savePrivilege(priDelete);
        privilegeService.savePrivilege(priView);

        roleService.saveRole(roleAdmin);

        user.addRole(roleAdmin);
        roleAdmin.addUser(user);

        roleService.saveRole(roleAdmin);
        userRepository.save(user);

        return "User " + user.getId() + " pays for course " + course.get().getId() + " successfully";
    }
    /**
     *
     * Get all transaction list
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getAllTransaction(Integer pageNo, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(transactions.getTotalPages())
                .data(transactions.stream().map(this::createTransactionResponse))
                .build();
    }

    /**
     *
     * Get transaction details
     *
     * @param transactionId Long
     * @return TransactionResponse
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionDetails(Long transactionId) {

        var transaction = transactionRepository.findById(transactionId);

        if (transaction.isEmpty()) {
            throw new ResourceNotFound("Transaction is not existed");
        }

        return createTransactionResponse(transaction.get());
    }

    /**
     *
     * Get user's transaction history
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> getTransactionHistory(Integer pageNo, Integer pageSize) {

        User user = userService.getUserFromAuthentication();

        Set<Transaction> transactions = user.getTransactions();
        List<Transaction> transactionList = transactions.stream().toList();

        int size = transactions.size();

        int totalPages = (int) Math.ceil(size * 1.0 / pageSize);

        return PageResponse.builder()
                .status(HttpStatus.OK.value())
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .data(transactionList.subList(Math.max(pageNo * pageSize, 0), Math.min((pageNo + 1) * pageSize, transactionList.size())).stream().map(this::createTransactionResponse))
                .build();
    }

    /**
     *
     * Create transaction response from transaction
     *
     * @param transaction Transaction
     * @return TransactionResponse
     */
    private TransactionResponse createTransactionResponse(Transaction transaction) {

        User user = userService.getUserFromAuthentication();

        return TransactionResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .cardHolder(user.getBankAccount().getCardHolder())
                .createAt(transaction.getCreatedAt())
                .updateAt(transaction.getUpdatedAt())
                .cardNumber(user.getBankAccount().getCardNumber())
                .paymentMethod(transaction.getPaymentMethod())
                .courseId(transaction.getCourse().getId())
                .userId(transaction.getUser().getId())
                .description(transaction.getDescription())
                .price(transaction.getPrice())
                .build();
    }
}
