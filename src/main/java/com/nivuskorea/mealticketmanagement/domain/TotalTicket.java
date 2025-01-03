package com.nivuskorea.mealticketmanagement.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class TotalTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOTAL_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "RECORD_ID")
    private MealRecord mealRecord;
    private Integer totalCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
