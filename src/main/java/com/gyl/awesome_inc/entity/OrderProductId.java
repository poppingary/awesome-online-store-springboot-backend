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
public class OrderProductId implements Serializable {
    private static final long serialVersionUID = -6437228592092430174L;

    @Column(name = "product_id", nullable = false, length = 30)
    private String productId;

    @Column(name = "order_id", nullable = false, length = 30)
    private String orderId;

    @Override
    public int hashCode() {
        return Objects.hash(productId, orderId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderProductId entity = (OrderProductId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.orderId, entity.orderId);
    }
}