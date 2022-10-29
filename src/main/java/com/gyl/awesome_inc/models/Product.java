package com.gyl.awesome_inc.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_product")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false, length = 30)
    private String id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "unit_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", nullable = false, precision = 2, scale = 2)
    private BigDecimal discount;

    @Column(name = "table_last_update", nullable = false)
    private Instant tableLastUpdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<OrderProduct> orderProducts = new LinkedHashSet<>();
}