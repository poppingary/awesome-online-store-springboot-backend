package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_order")
public class Order {
    @EmbeddedId
    private OrderId id;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ship_address_id", nullable = false, referencedColumnName = "ship_address_id")
    private ShipAddress shipAddress;

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "ship_mode", nullable = false, length = 30)
    private String shipMode;

    @Column(name = "order_priority", nullable = false, length = 30)
    private String orderPriority;

    @Column(name = "is_returned", nullable = false, length = 1)
    private String isReturned;

    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> OrderProducts = new LinkedHashSet<>();

}