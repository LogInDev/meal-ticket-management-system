package com.nivuskorea.mealticketmanagement.domain;

public enum MealStatus {
    LUNCHED, DINNER, ADDED, CANCELED;

    public static MealStatus from(String mealType) {
        return switch (mealType.toLowerCase()) {
            case "lunch" -> LUNCHED;
            case "dinner" -> DINNER;
            default -> throw new IllegalArgumentException("Invalid meal type: " + mealType);
        };
    }
}
