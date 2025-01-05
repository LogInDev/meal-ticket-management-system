package com.nivuskorea.mealticketmanagement.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
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

    protected TotalTicket() {
        //JPA가 사용할 기본 생성자
    }

    public TotalTicket(MealRecord mealRecord, Integer totalCount) {
        this.mealRecord = mealRecord;
        this.totalCount = totalCount;
    }

    // 식권 사용
    public void decrease(){
        if (this.totalCount > 0){
            this.totalCount--;
        }else{
            throw new IllegalStateException("Total count cannot be zero");
        }
    }

    // 식권 사용 취소
    public void increase(){
        this.totalCount++;
    }

    // 삭권 구매
    public void purchaseTicket(int amount){
        this.totalCount += amount;
    }
}
