package com.nivuskorea.mealticketmanagement.service;

import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryDto;
import com.nivuskorea.mealticketmanagement.repository.user.UserRepository;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MealRecordServiceTest {

    @Autowired
    private MealRecordService mealRecordService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("mealRecord 테이블에 식권 기록 추가")
    void addRecord(){
        // given - 사용자 생성 및 저장
        User user = new User("test", 1, EmploymentStatus.EMPLOYED);
        userRepository.save(user);

        // 중식 객체 생성
        MealStatus mealStatus = MealStatus.LUNCHED;

        // when - mealRecord 테이블에 식권 기록이 추가된다.
        MealRecord savedRecord = mealRecordService.addRecord(user.getId(), mealStatus, false);

        // then - 추가한 mealRecord 데이터와 기록이 일치한다.
        assertThat(savedRecord).isNotNull();
        assertThat(savedRecord.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedRecord.getMealStatus()).isEqualTo(MealStatus.LUNCHED);
        assertThat(savedRecord.getIsCanceled()).isFalse();
    }

    @Test
    @DisplayName("오늘 날짜 중식 식권 명단 생성")
    void getLunchRecord() {
        //given - 사용자와 중식 식권 기록 생성
        User user1 = new User("test", 7, EmploymentStatus.EMPLOYED);
        User user2 = new User("test2", 8, EmploymentStatus.EMPLOYED);
        User user3 = new User("test3", 9, EmploymentStatus.EMPLOYED);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        mealRecordService.addRecord(user1.getId(), MealStatus.LUNCHED, false);
        mealRecordService.addRecord(user2.getId(), MealStatus.LUNCHED, false);
        mealRecordService.addRecord(user3.getId(), MealStatus.DINNER, false);

        //when - 오늘 날짜 중식 식권 명단 조회
        List<MealRecordQueryDto> lunchRecords = mealRecordService.getLunchRecord(MealStatus.LUNCHED);

        //then - 조회한 중식 기록 검증
        assertThat(lunchRecords).isNotNull();
        assertThat(lunchRecords.size()).isEqualTo(6); // 중식, 석식 여부와 관계없이 재직 상태의 user는 다 출력되어야함.

        MealRecordQueryDto lunchRecord1 = lunchRecords.stream()
                .filter(record -> record.getId().equals(user1.getId()))
                .findFirst()
                .orElse(null);

        MealRecordQueryDto lunchRecord2 = lunchRecords.stream()
                .filter(record -> record.getId().equals(user2.getId()))
                .findFirst()
                .orElse(null);

        assertThat(lunchRecord1).isNotNull();
        assertThat(lunchRecord1.getMealStatus()).isEqualTo(MealStatus.LUNCHED);
        assertThat(lunchRecord1.getIsCanceled()).isFalse();

        assertThat(lunchRecord2).isNotNull();
        assertThat(lunchRecord2.getMealStatus()).isEqualTo(MealStatus.LUNCHED);
        assertThat(lunchRecord2.getIsCanceled()).isFalse();
    }
}