package com.application.elerna.service;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TransactionResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    boolean pay(PaymentRequest request);

    PageResponse<?> getAllTransaction(Integer pageNo, Integer pageSize);

    TransactionResponse getTransactionDetails(Long transactionId);

    PageResponse<?> getTransactionHistory(Integer pageNo, Integer pageSize);

}
