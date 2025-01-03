package com.nivuskorea.mealticketmanagement.domain;

import jakarta.persistence.*;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

}
