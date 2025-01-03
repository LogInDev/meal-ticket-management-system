package com.nivuskorea.mealticketmanagement.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class MealRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "USER_ID") // 외래 키 이름을 USER_ID로 지정
    private User user;

    @Enumerated(EnumType.STRING)
    private MealStatus mealStatus;
    private Boolean isCanceled = false;

    @OneToMany(mappedBy = "mealRecord")
    private List<TotalTicket> totalTickets = new ArrayList<>();
}
