package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tokens")
public class Token extends AbstractEntity<Long> {

    @Column(name="uuid")
    private String uuid;

    @Column(name="access_token")
    private String accessToken;

    @Column(name="refresh_token")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="acc_status")
    private boolean accStatus;

    @Column(name="ref_status")
    private boolean refStatus;

    @Column(name="acc_expiration_time")
    private Date accExpiration;

    @Column(name="ref_expiration_time")
    private Date refExpiration;

}
