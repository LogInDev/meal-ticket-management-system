package com.nivuskorea.mealticketmanagement.service;


import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.dto.UserForm;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryDto;
import com.nivuskorea.mealticketmanagement.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 추가된 사원의 사원번호로 중복 여부 체크
     * @param userForm 사용자의 이름, 사원번호, 재직상태 등을 담고 있는 폼 객체
     * @return 중복된 사원번호 + 수정된 부분 X -> True, 중복된 사원번호 + 수정된 부분 O -> False, 중복된 사원번호 X -> False
     */
    public boolean isDuplicateUser(UserForm userForm) {
        // 기존에 해당 사원번호로 존재하는 User 객체를 조회
        final User existingUser = userRepository.findUserByEmployeeNumber(Integer.valueOf(userForm.getEmployeeNumber()));

        if (existingUser != null) { // 중복된 사원번호가 있음
            // 기존 User와 비교하여 수정된 부분이 없으면 false 반환
            return existingUser.getName().equals(userForm.getName()) &&
                    existingUser.getEmploymentStatus() == userForm.getEmploymentStatus(); // 수정된 부분이 없으면 true 반환
        }
        return false; // 해당 사원번호의 User가 없으면 false 반환
    }

    /**
     * 변경된 사항으로 User update or insert
     * @param userForm 사원번호, 사원이름, 재직 상태를 가지고 있는 userForm 객체
     */
    @Transactional
    public void saveOrUpdateUser(UserForm userForm) {
        // UserForm에서 받은 데이터를 기반으로 새로운 User 객체를 생성
        User existingUser = userRepository.findUserByEmployeeNumber(Integer.valueOf(userForm.getEmployeeNumber()));
        if (existingUser != null) {
            existingUser.update(userForm.getName(), userForm.getEmploymentStatus());
        } else {
            existingUser = new User(userForm.getName(), Integer.valueOf(userForm.getEmployeeNumber()), EmploymentStatus.EMPLOYED);
            userRepository.save(existingUser);
        }

    }

    /**
     * 오늘 날짜 식권 사용 가능자 조회
     */
    public List<MealRecordQueryDto> getEligibleUsersForTodayMeal(MealStatus mealStatus) {
        // 오늘 날짜 데이터 범위 기준 생성
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return userRepository.findMealRecordsByUserId(mealStatus,startOfDay,endOfDay);
    }

}
