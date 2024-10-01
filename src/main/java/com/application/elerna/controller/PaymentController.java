package com.application.elerna.controller;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TransactionResponse;
import com.application.elerna.service.BankAccountService;
import com.application.elerna.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final BankAccountService bankAccountService;

    /**
     *
     * Send payment request for third-party api
     *
     * @param request PaymentRequest
     * @return ResponseData<Boolean>
     */
    @PostMapping("/pay")
    public ResponseData<String> payForCourse(@RequestBody PaymentRequest request) {

        return new ResponseData<>(HttpStatus.OK, "Pay for course", paymentService.pay(request));
    }

    /**
     *
     * Get all transactions list
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @GetMapping("/transaction/list")
    public PageResponse<?> getAllTransaction(Integer pageNo, Integer pageSize) {
        return paymentService.getAllTransaction(pageNo, pageSize);
    }

    /**
     *
     * Get transaction details
     *
     * @param transactionId Long
     * @return ResponseData<TransactionResponse>
     */
    @GetMapping("/transaction/details")
    public ResponseData<TransactionResponse> getTransactionDetails(Long transactionId) {
        return new ResponseData<>(HttpStatus.OK, "Get transaction details successfully", paymentService.getTransactionDetails(transactionId));
    }

    /**
     *
     * Get user's transaction history
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @GetMapping("/transaction/history")
    public PageResponse<?> getTransactionHistory(Integer pageNo, Integer pageSize) {

        return paymentService.getTransactionHistory(pageNo, pageSize);
    }

    /**
     *
     * Get bank account logs
     *
     * @param pageNo Integer
     * @param pageSize Integer
     * @return PageResponse
     */
    @GetMapping("/transaction/bank-logs")
    public PageResponse<?> getBankAccountLogs(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return bankAccountService.getBankAccountLogs(pageNo, pageSize);
    }

    /**
     *
     * Deposit money into bank account
     *
     * @param amount Double
     * @return ResponseData<String>
     */
    @PostMapping("/transaction/deposit")
    public ResponseData<String> deposit(@RequestParam Double amount) {
        return new ResponseData<>(HttpStatus.ACCEPTED, "Deposit money into bank account, amount = " + amount, bankAccountService.deposit(amount));
    }

}
