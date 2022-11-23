package com.gyl.awesome_inc.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data
@Embeddable
public class CustomerProductId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7567217923758476034L;

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;
}