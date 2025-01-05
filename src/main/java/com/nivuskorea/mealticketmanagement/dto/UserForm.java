package com.nivuskorea.mealticketmanagement.dto;

import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "사원번호는 필수 입력 항목입니다.")
    private Integer employeeNumber;
    private EmploymentStatus employmentStatus;


}
