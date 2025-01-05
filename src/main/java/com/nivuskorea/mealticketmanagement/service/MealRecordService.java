package com.nivuskorea.mealticketmanagement.service;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryDto;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryRepository;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordRepository;
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
public class MealRecordService {

    private final MealRecordRepository mealRecordRepository;
    private final UserRepository userRepository;
    private final MealRecordQueryRepository mealRecordQueryRepository;

    /**
     * 식권 기록 추가
     */
    @Transactional
    public MealRecord addRecord(Integer employeeNumber, MealStatus mealStatus, Boolean isCanceled) {
        // User 조회
        User savedUser = userRepository.findUserByEmployeeNumber(employeeNumber);
        if (savedUser == null) {
            throw new IllegalArgumentException("Employee number not found");
        }

        // MealRecord 생성 및 저장
        return mealRecordRepository.save(new MealRecord(savedUser,mealStatus,isCanceled));
    }

    /**
     * 오늘 날짜 중식 식권 대장 조회
     */
    public List<MealRecordQueryDto> getLunchRecord(MealStatus mealStatus) {
        // 오늘 날짜 데이터 범위 기준 생성
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return mealRecordQueryRepository.findMealRecordsByUserId(mealStatus,startOfDay,endOfDay);
    }


}
