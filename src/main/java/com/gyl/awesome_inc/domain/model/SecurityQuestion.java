package com.gyl.awesome_inc.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fa22_sg_security_question")
public class SecurityQuestion {
    @Id
    @Column(name = "security_question_id", nullable = false, length = 30)
    private String id;

    @Column(name = "security_question", nullable = false, length = 100)
    private String securityQuestion;

    @OneToMany(mappedBy = "securityQuestion")
    private Set<Customer> customers = new LinkedHashSet<>();
}