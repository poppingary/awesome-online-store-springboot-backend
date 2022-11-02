package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_order")
public class Order {
    @Id
    @Column(name = "order_id", nullable = false, length = 30)
    private String id;

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "ship_mode", nullable = false, length = 30)
    private String shipMode;

    @Column(name = "order_priority", nullable = false, length = 30)
    private String orderPriority;

    @Column(name = "is_returned", nullable = false, length = 1)
    private String isReturned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}