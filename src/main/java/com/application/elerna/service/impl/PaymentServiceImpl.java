package com.application.elerna.service.impl;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TransactionResponse;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Course;
import com.application.elerna.model.Transaction;
import com.application.elerna.model.User;
import com.application.elerna.repository.CourseRepository;
import com.application.elerna.repository.TransactionRepository;
import com.application.elerna.repository.UserRepository;
import com.application.elerna.service.PaymentService;
import com.application.elerna.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    /**
     *
     * Send payment request to third-party api
     *
     * @param request PaymentRequest
     * @return Boolean
     */
    @Override
    public boolean pay(PaymentRequest request) {

        // create payment request by third-party api

        // suppose that it successfully

        boolean isSuccessfully = true;

        var course = courseRepository.findById(request.getCourseId());

        if (course.isEmpty() || !course.get().isStatus()) {

            throw new ResourceNotFound("Course is not ready, id = " + request.getCourseId());

        }

        Course currentCourse = course.get();

        User user = userService.getUserFromAuthentication();

        Transaction transaction = Transaction.builder()
                .course(currentCourse)
                .user(user)
                .description(request.getDescription())
                .paymentMethod(request.getPaymentMethod())
                .price(request.getPrice())
                .status("done")
                .cardNumber(user.getCardNumber())
                .build();

        transactionRepository.save(transaction);

        currentCourse.addTransaction(transaction);
        user.addTransaction(transaction);

        courseRepository.save(currentCourse);
        userRepository.save(user);

        return isSuccessfully;
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
                .cardHolder(user.getFirstName() + " " + user.getLastName())
                .createAt(transaction.getCreatedAt())
                .updateAt(transaction.getUpdatedAt())
                .cardNumber(transaction.getCardNumber())
                .paymentMethod(transaction.getPaymentMethod())
                .courseId(transaction.getCourse().getId())
                .userId(transaction.getUser().getId())
                .description(transaction.getDescription())
                .price(transaction.getPrice())
                .build();
    }
}
