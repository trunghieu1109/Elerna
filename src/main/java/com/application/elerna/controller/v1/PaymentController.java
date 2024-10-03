package com.application.elerna.controller.v1;

import com.application.elerna.dto.request.PaymentRequest;
import com.application.elerna.dto.response.PageResponse;
import com.application.elerna.dto.response.ResponseData;
import com.application.elerna.dto.response.TransactionResponse;
import com.application.elerna.service.BankAccountService;
import com.application.elerna.service.PaymentService;
import com.application.elerna.utils.EndpointDescription;
import com.application.elerna.utils.ResponseExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@Tag(name="Payment Management Controller", description = "There are functions related to transaction or payment")
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
    @Operation(summary = "Pay for course", description = EndpointDescription.payDescription,
            responses = { @ApiResponse(responseCode = "200", description = "Pay for course successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.payExample))
            )})
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
    @Operation(summary = "Get all transaction list", description = EndpointDescription.getAllTransactionDescription,
            responses = { @ApiResponse(responseCode = "200", description = "Get all transaction list successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getAllTransactionExample))
            )})
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
    @Operation(summary = "Get transaction's details", description = EndpointDescription.getTransactionDetail,
            responses = { @ApiResponse(responseCode = "200", description = "Get transaction's details successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getTransactionDetailsExample))
            )})
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
    @Operation(summary = "Get transaction's history", description = EndpointDescription.getTransactionHistory,
            responses = { @ApiResponse(responseCode = "200", description = "Get transaction's history successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getTransactionHistoryExample))
            )})
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
    @Operation(summary = "Get bank account's logs", description = EndpointDescription.getBankAccountLogs,
            responses = { @ApiResponse(responseCode = "200", description = "Get bank account logs successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.getBankAccountLogs))
            )})
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
    @Operation(summary = "Deposit money to bank account", description = EndpointDescription.depositDescription,
            responses = { @ApiResponse(responseCode = "200", description = "Deposit money to account successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = ResponseExample.depositExample))
            )})
    @PostMapping("/transaction/deposit")
    public ResponseData<String> deposit(@RequestParam Double amount) {
        return new ResponseData<>(HttpStatus.ACCEPTED, "Deposit money into bank account, amount = " + amount, bankAccountService.deposit(amount));
    }

}
