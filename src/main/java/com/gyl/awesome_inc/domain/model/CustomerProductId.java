package com.gyl.awesome_inc.domain.model;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CustomerProductId implements Serializable {
    private static final long serialVersionUID = -7567217923758476034L;
    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;
    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, customerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CustomerProductId entity = (CustomerProductId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.customerId, entity.customerId);
    }
}