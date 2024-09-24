package com.application.elerna.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tokens")
public class Token extends AbstractEntity<Long> {

    @Column(name="access_token")
    private String accessToken;

    @Column(name="refresh_token")
    private String refreshToken;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = false)
    @JoinColumn(name="user_id")
    private User user;

    @JoinColumn(name="status")
    private boolean status;

}
