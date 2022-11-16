package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_password_reset_token")
public class PasswordResetToken {
    @Id
    @Column(name = "token", nullable = false, length = 50)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;

    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;
}