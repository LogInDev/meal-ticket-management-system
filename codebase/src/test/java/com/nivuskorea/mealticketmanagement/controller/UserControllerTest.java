package com.nivuskorea.mealticketmanagement.controller;

import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.dto.UserForm;
import com.nivuskorea.mealticketmanagement.repository.user.UserRepository;
import com.nivuskorea.mealticketmanagement.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("사용자 추가 성공한 경우 프로세스")
    void addUser_success() throws Exception {
        // Given
        String username = "test";
        int employeeNumber = 1;
        EmploymentStatus employmentStatus = EmploymentStatus.EMPLOYED;

        // When
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + username + "\", \"employeeNumber\":" + employeeNumber + ", \"employmentStatus\": \"" + employmentStatus + "\"}"))
                .andExpect(status().isCreated());

        // Then
        User createdUser = userRepository.findUserByEmployeeNumber(employeeNumber);
        assertThat(createdUser).isNotNull(); // 사용자가 저장되었는지 확인
        assertThat(createdUser.getName()).isEqualTo(username); // 이름이 일치하는지 확인
        assertThat(createdUser.getEmployeeNumber()).isEqualTo(employeeNumber);  // 사원번호가 일치하는지 확인
        assertThat(createdUser.getEmploymentStatus()).isEqualTo(employmentStatus); // 재직 상태가 일치하는지 확인
    }

    @Test
    @DisplayName("중복되는 사원번호 예외발생")
    void duplicateUser_throwsDuplicateUserException() throws Exception {
        // Given - 기존 사용자 저장
        Integer duplicateEmployeeNumber = 1;
        UserForm userForm = new UserForm();
        userForm.setEmployeeNumber(duplicateEmployeeNumber);

        // Mock UserService 동작 설정
        Mockito.when(userService.isDuplicateEmployeeNumber(duplicateEmployeeNumber)).thenReturn(true);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/use")
                .param("employeeNumber", String.valueOf(duplicateEmployeeNumber))
                .param("mealType", "lunch")
                .flashAttr("userForm", userForm))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("userForm", "employeeNumber"))
                .andExpect(model().attributeHasFieldErrorCode("userForm", "employeeNumber", "duplicate"))
                .andReturn();


    }

}
