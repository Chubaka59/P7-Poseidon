package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "curvepoint")
@Data
@NoArgsConstructor
public class CurvePoint implements UpdatableEntity<CurvePoint> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Min(0)
    private Integer curveId;
    private Timestamp asOfDate;
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount can not be equal less than 0")
    private Double term;
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount can not be equal less than 0")
    private Double curveValue;
    private Timestamp creationDate;

    public CurvePoint(Integer curveId, Double term, Double curveValue){
        this.curveId = curveId;
        this.term = term;
        this.curveValue = curveValue;
    }

    @Override
    public CurvePoint update(CurvePoint entity) {
        this.curveId = entity.curveId;
        this.asOfDate = entity.asOfDate;
        this.term = entity.term;
        this.curveValue = entity.curveValue;
        this.creationDate = entity.creationDate;
        return this;
    }
}
