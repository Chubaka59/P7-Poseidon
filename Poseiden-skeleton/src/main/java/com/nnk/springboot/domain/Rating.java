package com.nnk.springboot.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rating")
@Data
public class Rating {
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
