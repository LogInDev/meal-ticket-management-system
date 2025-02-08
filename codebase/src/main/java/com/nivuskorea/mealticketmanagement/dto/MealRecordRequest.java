package com.nivuskorea.mealticketmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealRecordRequest {
    private Long userId;
    private String mealType;
    private Boolean isCanceled;
}
