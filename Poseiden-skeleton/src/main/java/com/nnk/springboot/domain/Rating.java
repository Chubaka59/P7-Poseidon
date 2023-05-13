package com.nnk.springboot.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {
    // TODO: Map columns in data table RATING with corresponding java fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String moodysRating;
    @Column
    private String sandPRating;
    @Column
    private String fitchRating;
    @Column
    private Integer orderNumber;
}
