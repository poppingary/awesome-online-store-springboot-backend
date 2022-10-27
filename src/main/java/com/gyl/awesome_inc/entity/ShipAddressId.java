package com.gyl.awesome_inc.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ShipAddressId implements Serializable {
    private static final long serialVersionUID = -3770428377293071143L;

    @Column(name = "ship_address_id", nullable = false, length = 30)
    private String shipAddressId;

    @Column(name = "customer_id", nullable = false, length = 30)
    private String customerId;

    @Override
    public int hashCode() {
        return Objects.hash(customerId, shipAddressId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ShipAddressId entity = (ShipAddressId) o;
        return Objects.equals(this.customerId, entity.customerId) &&
                Objects.equals(this.shipAddressId, entity.shipAddressId);
    }
}