package com.nivuskorea.mealticketmanagement.service;

import com.nivuskorea.mealticketmanagement.domain.*;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordQueryDto;
import com.nivuskorea.mealticketmanagement.repository.mealRecord.MealRecordRepository;
import com.nivuskorea.mealticketmanagement.repository.totalTicket.TotalTicketRepository;
import com.nivuskorea.mealticketmanagement.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class MealRecordServiceTest {

    @InjectMocks
    private MealRecordService mealRecordService;

    @Mock
    private MealRecordRepository mealRecordRepository;

    @Mock
    private TotalTicketRepository totalTicketRepository;

    @Autowired
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private Long userId;
    private MealStatus mealStatus;
    private LocalDate today;
    private LocalDateTime startOfToday;
    private LocalDateTime startOfTomorrow;

    @BeforeEach
    public void setUp() {
        userId = 123L;
        mealStatus = MealStatus.LUNCHED;
        today = LocalDate.now();
        startOfToday = today.atStartOfDay();
        startOfTomorrow = today.plusDays(1).atStartOfDay();
    }

    @Test
    @DisplayName("식권 기록이 없을 경우 식권 사용 기록 추가")
    public void toggleMealTicket_whenNoRecordExists_createsNewRecord() {
        // given: 오늘 사용 기록이 없음
        when(mealRecordRepository.findByUserIdAndCreatedAtBetween(eq(userId), eq(startOfToday), eq(startOfTomorrow)))
                .thenReturn(Optional.empty());

        // given: userRepository.findById(userId)로 사용자 조회
        final User user = new User("TestUser", 456, EmploymentStatus.EMPLOYED);
        // 테스트용으로 user 객체의 id를 강제로 설정(userId는 예: 123L)
        ReflectionTestUtils.setField(user, "id", userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // (실제 엔티티에서는 user.getId()가 null이면 안되므로, 테스트에서는 user 객체가 제대로 설정되었다고 가정
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // given: 새 MealRecord가 저장될 때 id가 할당됨 (Mockito Answer 사용)
        when(mealRecordRepository.save(any(MealRecord.class))).thenAnswer(invocation -> {
            MealRecord record = invocation.getArgument(0);
            //테스트용 id 할당
            ReflectionTestUtils.setField(record, "id", 1L);
            return record;
        });

        // given: totalTicketRepository에서 이전 total_count가 없으면 기본 100을 사용
        when(totalTicketRepository.findTopByOrderByCreatedAtDesc())
                .thenReturn(Optional.empty());
        when(totalTicketRepository.save(any(TotalTicket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when: 토글 기능 호출(사용처리)
        mealRecordService.toggleMealTicket(userId, mealStatus);

        // then: 새로운 MealRecord가 생성되어 저장됨
        Mockito.verify(mealRecordRepository, Mockito.times(1)).save(Mockito.argThat(record ->
                record.getUser().equals(user) &&
                record.getMealStatus().equals(mealStatus) &&
                Boolean.FALSE.equals(record.getIsCanceled())
        ));

        // then: TotalTicket이 저장되며 total_count는 99 ( 100 - 1)로 설정됨.
        Mockito.verify(totalTicketRepository, Mockito.times(1)).save(Mockito.argThat(totalTicket ->
                totalTicket.getTotalCount() == 99
        ));

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
        List<MealRecordQueryDto> lunchRecords = userService.getEligibleUsersForTodayMeal(MealStatus.LUNCHED);

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