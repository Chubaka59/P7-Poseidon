package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rulename")
@Data
@NoArgsConstructor
public class RuleName implements UpdatableEntity<RuleName> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String json;
    @NotBlank
    private String template;
    @NotBlank
    private String sqlStr;
    @NotBlank
    private String sqlPart;

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart){
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }

    @Override
    public RuleName update(RuleName entity) {
        this.name = entity.name;
        this.description = entity.description;
        this.json = entity.json;
        this.template = entity.template;
        this.sqlStr = entity.sqlStr;
        this.sqlPart = entity.sqlPart;
        return this;
    }
}
