package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_ship_address")
public class ShipAddress {
    @Id
    @Column(name = "ship_address_id", nullable = false, length = 50)
    private String id;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "is_primary", nullable = false, length = 1)
    private String isPrimary;
}