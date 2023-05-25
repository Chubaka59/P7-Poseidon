package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
public class Rating implements UpdatableEntity<Rating> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String moodysRating;
    @NotBlank
    private String sandPRating;
    @NotBlank
    private String fitchRating;
    @NotNull
    @Min(1)
    private Integer orderNumber;

    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber){
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    @Override
    public Rating update(Rating entity) {
        this.moodysRating = entity.moodysRating;
        this.sandPRating = entity.sandPRating;
        this.fitchRating = entity.fitchRating;
        this.orderNumber = entity.orderNumber;
        return this;
    }
}
