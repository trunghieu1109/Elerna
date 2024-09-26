package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction extends AbstractEntity<Long> {

    @Column(name="price")
    private Double price;

    @Column(name="payment_method")
    private String paymentMethod;

    @Column(name="description")
    private String description;

    @Column(name="status")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private Course course;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    private Set<TransactionLog> transactionLogs = new HashSet<>();

}
