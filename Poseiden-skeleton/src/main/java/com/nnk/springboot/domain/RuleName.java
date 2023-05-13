package com.nnk.springboot.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rulename")
public class RuleName {
    // TODO: Map columns in data table RULENAME with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String json;
    @Column
    private String template;
    @Column
    private String sqlStr;
    @Column
    private String sqlPart;

}
