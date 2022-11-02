package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_category")
public class Category {
    @Id
    @Column(name = "category_id", nullable = false, length = 30)
    private String id;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "subcategory", nullable = false, length = 20)
    private String subcategory;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new LinkedHashSet<>();
}