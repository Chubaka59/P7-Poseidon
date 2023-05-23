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
    private Integer curveId;
    private Timestamp asOfDate;
    private Double term;
    private Double curveValue;
    private Timestamp creationDate;

    public CurvePoint(Integer curveId, Double term, Double curveValue){
        this.curveId = curveId;
        this.term = term;
        this.curveValue = curveValue;
    }
}
