package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transactions_log")
public class TransactionLog extends AbstractEntity<Long> {

    @Column(name="message_type")
    private String messageType;

    @Column(name="message")
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transaction_id")
    private Transaction transaction;



}
