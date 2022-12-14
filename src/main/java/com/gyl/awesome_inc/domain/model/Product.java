package com.gyl.awesome_inc.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_product")
public class Product {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "product_id", nullable = false, length = 50)
    private String id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "unit_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount", nullable = false, precision = 2, scale = 2)
    private BigDecimal discount;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<OrderProduct> orderProducts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<CustomerProduct> CustomerProducts = new LinkedHashSet<>();
}