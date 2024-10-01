package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="bank_accounts")
public class BankAccount extends AbstractEntity<Long> {

    public static double MAINTAINING_AMOUNT = 2.0;

    @Column(name="card_holder")
    private String cardHolder;

    @Column(name="card_number")
    private String cardNumber;

    @Column(name="amount")
    private Double amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
    private Set<BankAccountLog> bankAccountLogs = new HashSet<>();

    public void addBankAccountLog(BankAccountLog bankAccountLog) {
        this.bankAccountLogs.add(bankAccountLog);
    }

}
