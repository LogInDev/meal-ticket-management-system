package com.nivuskorea.mealticketmanagement.repository.user;

import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmployeeNumber(Integer employeeNumber);


    User findUserByEmployeeNumber(Integer employeeNumber);

    List<User> findUserByEmploymentStatus(EmploymentStatus employmentStatus);
}
