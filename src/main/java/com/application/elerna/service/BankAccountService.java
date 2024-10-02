package com.application.elerna.service;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.request.SignUpRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.model.BankAccount;
import com.application.elerna.model.User;
import org.springframework.stereotype.Service;

@Service
public interface BankAccountService {

    BankAccount createBankAccount(User user);

    boolean pay(PaymentRequest request);

    PageResponse<?> getBankAccountLogs(Integer pageNo, Integer pageSize);

    String deposit(Double amount);

    void saveBankAccount(BankAccount account);
}
