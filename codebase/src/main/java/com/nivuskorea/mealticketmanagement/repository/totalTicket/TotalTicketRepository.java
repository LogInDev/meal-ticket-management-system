package com.nivuskorea.mealticketmanagement.repository.totalTicket;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.TotalTicket;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TotalTicketRepository extends JpaRepository<TotalTicket, Long> {
    Optional<TotalTicket> findTopByOrderByCreatedAtDesc();

}
