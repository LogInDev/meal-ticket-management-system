package com.nivuskorea.mealticketmanagement.model.entity;

import com.nivuskorea.mealticketmanagement.model.config.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class MealRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID") // 외래 키 이름을 USER_ID로 지정
    private User user;

    @Enumerated(EnumType.STRING)
    private MealStatus mealStatus;
    private Boolean isCanceled = false;

    @OneToOne(mappedBy = "mealRecord", fetch = LAZY)
    private TotalTicket totalTicket;
}
