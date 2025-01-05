package com.nivuskorea.mealticketmanagement.service;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.TotalTicket;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryDto;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryRepository;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordRepository;
import com.nivuskorea.mealticketmanagement.repository.totalTicket.TotalTicketRepository;
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
    private final TotalTicketRepository totalTicketRepository;

    /**
     * 식권 기록 추가
     */
    @Transactional
    public void addRecord(Long userId, MealStatus mealStatus, Boolean isCanceled) {
        // User 조회
        User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // MealRecord 조회 또는 생성
        MealRecord mealRecord = getOrCreatedMealRecord(mealStatus, isCanceled, savedUser);

        // TotalTicket 관리
        manageTotalTicket(mealRecord, isCanceled);
    }

    private MealRecord getOrCreatedMealRecord(MealStatus mealStatus, Boolean isCanceled, User savedUser) {
        MealRecord mealRecord = mealRecordRepository.findByUserAndMealStatus(savedUser, mealStatus)
                .orElseGet(() -> new MealRecord(savedUser, mealStatus, isCanceled));

        // MealRecord 상태 업데이트
        if (isCanceled) {
            mealRecord.cancelMeal();
        } else {
            mealRecord.useMeal();
        }
        mealRecordRepository.save(mealRecord);
        return mealRecord;
    }

    private void manageTotalTicket(MealRecord mealRecord, Boolean isCanceled) {
        TotalTicket latestTicket = totalTicketRepository.findTopByOrderByCreatedAtDesc()
                .orElseGet(() -> new TotalTicket(mealRecord, 100));

        // 현재 totalCount 가져오기
        int currentCount = latestTicket.getTotalCount();

        TotalTicket addTotalTicket = new TotalTicket(mealRecord, currentCount);

        // 상태에 따라 업데이트
        if (isCanceled) {
            addTotalTicket.decrease();
        } else {
            addTotalTicket.increase();
        }

        // 새로운 TotalTicket 저장
        totalTicketRepository.save(addTotalTicket);

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
