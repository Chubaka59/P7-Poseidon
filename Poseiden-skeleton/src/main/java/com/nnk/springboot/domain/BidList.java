package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Data
@NoArgsConstructor
public class BidList implements UpdatableEntity<BidList>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer BidListId;
    @NotBlank
    private String account;
    @NotBlank
    private String type;
    @DecimalMin(value = "0.01", message = "Amount can not be equal less than 0")
    @NotNull
    private Double bidQuantity;
    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;
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

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    public BidList update(BidList entity){
        this.account = entity.account;
        this.type = entity.type;
        this.bidQuantity = entity.bidQuantity;
        this.askQuantity = entity.askQuantity;
        this.bid = entity.bid;
        this.ask = entity.ask;
        this.benchmark = entity.benchmark;
        this.bidListDate = entity.bidListDate;
        this.commentary = entity.commentary;
        this.security = entity.security;
        this.status = entity.status;
        this.trader = entity.trader;
        this.book = entity.book;
        this.creationName = entity.creationName;
        this.creationDate = entity.creationDate;
        this.revisionName = entity.revisionName;
        this.revisionDate = entity.revisionDate;
        this.dealName = entity.dealName;
        this.dealType = entity.dealType;
        this.sourceListId = entity.sourceListId;
        this.side = entity.side;
        return this;
    }
}
