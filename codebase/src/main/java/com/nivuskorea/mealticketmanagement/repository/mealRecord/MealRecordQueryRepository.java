package com.nivuskorea.mealticketmanagement.repository.mealRecord;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRecordQueryRepository extends JpaRepository<MealRecord, Long> {



}
