package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="bank_account_logs")
public class BankAccountLog extends AbstractEntity<Long> {

    @Column(name="message_type")
    private String messageType;

    @Column(name="message")
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private BankAccount bankAccount;



}
