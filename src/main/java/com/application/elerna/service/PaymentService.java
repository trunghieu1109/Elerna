package com.application.elerna.service;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.TransactionResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    boolean pay(PaymentRequest request);

    @PreAuthorize("hasPermission(-1, 'transaction', 'all')")
    PageResponse<?> getAllTransaction(Integer pageNo, Integer pageSize);

    @PreAuthorize("hasPermission(#transactionId, 'transaction', 'view')")
    TransactionResponse getTransactionDetails(Long transactionId);

    PageResponse<?> getTransactionHistory(Integer pageNo, Integer pageSize);

}
