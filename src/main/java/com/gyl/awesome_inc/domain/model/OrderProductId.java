package com.gyl.awesome_inc.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class OrderProductId implements Serializable {
    private static final long serialVersionUID = -6437228592092430174L;

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "order_id", nullable = false, length = 50)
    private String orderId;
}