package com.nivuskorea.mealticketmanagement.service;


import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 추가
     */
    @Transactional
    public void addUser(User user) {

        User savedUser = new User(user.getName(), user.getEmployeeNumber(), user.getEmploymentStatus());
        userRepository.save(savedUser);
    }

    // 중복되는 사원번호 유무 여부 체크
    public boolean isDuplicateEmployeeNumber(Integer employeeNumber) {
        return userRepository.existsByEmployeeNumber(employeeNumber);
    }


    /**
     * 'EMPLOYED'상태인 사용자 전체 조회하기
     */
    public List<User> getEmployedUsers() {
        return userRepository.findUserByEmploymentStatus(EmploymentStatus.EMPLOYED);
    }
}
