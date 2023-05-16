package com.nnk.springboot.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
@Data
@NoArgsConstructor
public class CurvePoint {
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

    public CurvePoint(Integer curveId, Double term, Double value){
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }
}
