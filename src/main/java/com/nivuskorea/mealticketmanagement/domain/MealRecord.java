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
    private Boolean isCanceled;

    @OneToMany(mappedBy = "mealRecord")
    private List<TotalTicket> totalTickets = new ArrayList<>();

    protected MealRecord() {
        //JPA가 사용하는 기본 생성자
    }

    public MealRecord(User user, MealStatus mealStatus, Boolean isCanceled) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        this.user = user;
        this.mealStatus = mealStatus;
        this.isCanceled = isCanceled;
    }

    // 식권 사용 상태 변경
    public void cancelMeal() {
        this.isCanceled = true;
    }
    public void useMeal(){
        this.isCanceled = false;
    }
}
