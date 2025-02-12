package com.nivuskorea.mealticketmanagement.service;

import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.TotalTicket;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordRepository;
import com.nivuskorea.mealticketmanagement.repository.totalTicket.TotalTicketRepository;
import com.nivuskorea.mealticketmanagement.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MealRecordService {

    private final MealRecordRepository mealRecordRepository;
    private final UserRepository userRepository;
    private final TotalTicketRepository totalTicketRepository;

    @Transactional
    public void toggleMealTicket(Long userId, MealStatus mealStatus) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime startOfTomorrow = today.plusDays(1).atStartOfDay();
        // 오늘 사용 기록이 존재하는지 확인
        final Optional<MealRecord> optionalRecord = mealRecordRepository.findByUserIdAndCreatedAtBetween(userId, startOfToday, startOfTomorrow);

        if (optionalRecord.isEmpty()) {
            // Senario 1: 사용 처리 (오늘 식권 기록 없음)
            final MealRecord newRecord = userRepository.findById(userId)
                    .map(user -> { // userId가 있을 경우 식권 사용 처리
                        final MealRecord record = new MealRecord(user, mealStatus, false);
                        return mealRecordRepository.save(record);
                    })
                    .orElse(null);

            final Integer prevTotalCnt = totalTicketRepository.findTopByOrderByCreatedAtDesc()
                    .map(TotalTicket::getTotalCount)
                    .orElse(100);
            final TotalTicket newTotal = new TotalTicket(newRecord, prevTotalCnt);
            newTotal.decrease();    // 식권 사용
            totalTicketRepository.save(newTotal);
        }else { // 오늘 식권 기록이 있는 경우
            final MealRecord mealRecord = optionalRecord.get();
            if (!mealRecord.getIsCanceled()) {  // 오늘 식권을 사용한 경우
                // Senario 2: 사용취소 처리 (현재 사용 중)
                mealRecord.cancelMeal();
                mealRecordRepository.save(mealRecord);

                int prevTotalCnt = totalTicketRepository.findTopByOrderByCreatedAtDesc()
                        .map(TotalTicket::getTotalCount)
                        .orElse(100);
                TotalTicket newTotal = new TotalTicket(mealRecord, prevTotalCnt);
                newTotal.increase();
                totalTicketRepository.save(newTotal);
            }else { // 오늘 식권을 사용하지 않은경우
                // Senario 3: 재사용 처리(이미 사용 취소된 상태)
                mealRecord.useMeal();
                mealRecordRepository.save(mealRecord);

                int prevTotalCnt = totalTicketRepository.findTopByOrderByCreatedAtDesc()
                        .map(TotalTicket::getTotalCount)
                        .orElse(100);
                TotalTicket newTotal = new TotalTicket(mealRecord, prevTotalCnt);
                newTotal.decrease();
                totalTicketRepository.save(newTotal);
            }
        }
    }


    /**
     * 식권 기록 추가
     *
     * @return
     */
    @Transactional
    public MealRecord addRecord(Long userId, MealStatus mealStatus, Boolean isCanceled) {
        // User 조회
        User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // MealRecord 조회 또는 생성
        MealRecord mealRecord = getOrCreatedMealRecord(mealStatus, isCanceled, savedUser);

        // TotalTicket 관리
        manageTotalTicket(mealRecord, isCanceled);
        return mealRecord;
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



}
