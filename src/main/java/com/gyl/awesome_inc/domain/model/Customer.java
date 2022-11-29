package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_customer")
public class Customer {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "customer_id", nullable = false, length = 50)
    private String id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(name = "segment", nullable = false, length = 20)
    private String segment;

    @Column(name = "security_answer", nullable = false, length = 100)
    private String securityAnswer;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "security_question_id", nullable = false)
    private SecurityQuestion securityQuestion;

    @OneToMany(mappedBy = "customer")
    private Set<CreditCard> creditCards = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<ShipAddress> shipAddresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<CustomerProduct> CustomerProducts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "customer")
    private Set<PasswordResetToken> PasswordResetTokens = new LinkedHashSet<>();
}