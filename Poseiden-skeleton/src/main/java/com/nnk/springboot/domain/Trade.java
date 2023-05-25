package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "trade")
@Data
@NoArgsConstructor
public class Trade implements UpdatableEntity<Trade> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeId;
    @NotBlank
    private String account;
    @NotBlank
    private String type;
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount can not be equal less than 0")
    private Double buyQuantity;
    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
    private String benchmark;
    private Timestamp tradeDate;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    private Timestamp creationDate;
    private String revisionName;
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;

    public Trade(String account, String type, Double buyQuantity){
        this.account = account;
        this.type = type;
        this.buyQuantity = buyQuantity;
    }


    @Override
    public Trade update(Trade entity) {
        this.account = entity.account;
        this.type = entity.type;
        return this;
    }
}
