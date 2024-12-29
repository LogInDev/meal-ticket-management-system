package com.nivuskorea.mealticketmanagement.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class TotalTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEAL_RECORD_ID")
    private MealRecord mealRecord;
    private Integer totalCount;

    @Column(updatable = false)
    private LocalDateTime createdAt;
}
