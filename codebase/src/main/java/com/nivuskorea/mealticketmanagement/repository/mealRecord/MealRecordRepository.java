package com.nivuskorea.mealticketmanagement.repository.mealRecord;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {

    Optional<MealRecord> findByUserAndMealStatus(User user, MealStatus mealStatus);

}
