package com.nivuskorea.mealticketmanagement.model.entity;

import com.nivuskorea.mealticketmanagement.model.config.BaseEntity;
import jakarta.persistence.*;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private EmplymentStatus employmentStatus;

}
