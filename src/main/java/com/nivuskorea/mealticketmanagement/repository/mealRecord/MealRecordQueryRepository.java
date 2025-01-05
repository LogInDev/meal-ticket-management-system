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

    @Query("""
        SELECT new com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryDto(
            u.id, u.name, u.employeeNumber, mr.mealStatus, mr.isCanceled, mr.createdAt, mr.updatedAt
        )
        FROM User u
        LEFT JOIN MealRecord mr
            ON u.id = mr.user.id
            AND mr.createdAt BETWEEN :startOfDay AND :endOfDay
            AND mr.mealStatus = :mealStatus
        WHERE u.employmentStatus = 'EMPLOYED'
    """)
    List<MealRecordQueryDto> findMealRecordsByUserId(
            @Param("mealStatus") MealStatus mealStatus,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

}
