package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_order")
public class Order {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "order_id", nullable = false, length = 50)
    private String id;

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "ship_mode", nullable = false, length = 30)
    private String shipMode;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "region", nullable = false, length = 20)
    private String region;

    @Column(name = "market", nullable = false, length = 20)
    private String market;

    @Column(name = "order_priority", nullable = false, length = 30)
    private String orderPriority;

    @Column(name = "is_returned", nullable = false, length = 1)
    private String isReturned;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> OrderProducts = new LinkedHashSet<>();
}