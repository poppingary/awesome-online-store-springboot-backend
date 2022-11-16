package com.gyl.awesome_inc.domain.model;

import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class OrderId implements Serializable {
    private static final long serialVersionUID = 7862194996596622497L;

    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;

    @Column(name = "ship_address_id", nullable = false, length = 50)
    private String shipAddressId;
}