package com.nivuskorea.mealticketmanagement.repository.mealRecord;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@EqualsAndHashCode(of = "id")
public class MealRecordQueryDto {
    private Long id; // userId
    private String name;
    private Integer employeeNumber;
    private MealStatus mealStatus;
    private Boolean isCanceled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    MealRecordQueryDto(MealRecord mealRecord) {
        //JPA에서 사용할 기본 생성자
    }

    MealRecordQueryDto(Long id, String name, Integer employeeNumber,MealStatus mealStatus, Boolean isCanceled, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.mealStatus = mealStatus;
        this.isCanceled = isCanceled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 화면에 표시할 updateAt 값 처리
    public String getDisplayUpdateAt() {
        LocalDateTime dateTime = (updatedAt != null) ? updatedAt : createdAt;
        return (dateTime != null) ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : null;
    }

}
