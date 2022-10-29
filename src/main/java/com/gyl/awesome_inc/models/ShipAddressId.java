package com.gyl.awesome_inc.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ShipAddressId implements Serializable {
    private static final long serialVersionUID = -3770428377293071143L;

    @Column(name = "ship_address_id", nullable = false, length = 30)
    private String shipAddressId;

    @Column(name = "customer_id", nullable = false, length = 30)
    private String customerId;
}