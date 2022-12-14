package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_credit_card")
public class CreditCard {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "credit_card_id", nullable = false, length = 50)
    private String id;

    @Column(name = "cardholder_first_name", nullable = false, length = 20)
    private String cardholderFirstName;

    @Column(name = "cardholder_last_name", nullable = false, length = 20)
    private String cardholderLastName;

    @Column(name = "card_number", nullable = false, length = 30)
    private String cardNumber;

    @Column(name = "expired_date", nullable = false, length = 10)
    private String expiredDate;

    @Column(name = "cvv", nullable = false, length = 10)
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}