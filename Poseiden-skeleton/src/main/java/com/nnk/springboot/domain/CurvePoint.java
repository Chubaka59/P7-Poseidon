package com.nnk.springboot.domain;


import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {
    // TODO: Map columns in data table CURVEPOINT with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer curveId;
    @Column
    private Timestamp asOfDate;
    @Column
    private Double term;
    @Column
    private Double value;
    @Column
    private Timestamp creationDate;
}
