package com.nnk.springboot.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Data
public class BidList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String account;
    @Column
    private String type;
    @Column
    private Double bidQuantity;
    @Column
    private Double askQuantity;
    @Column
    private Double bid;
    @Column
    private Double ask;
    @Column
    private String benchmark;
    @Column
    private Timestamp bidListDate;
    @Column
    private String commentary;
    @Column
    private String security;
    @Column
    private String status;
    @Column
    private String trader;
    @Column
    private String book;
    @Column
    private String creationName;
    @Column
    private Timestamp creationDate;
    @Column
    private String revisionName;
    @Column
    private Timestamp revisionDate;
    @Column
    private String dealName;
    @Column
    private String dealType;
    @Column
    private String sourceListId;
    @Column
    private String side;
}
