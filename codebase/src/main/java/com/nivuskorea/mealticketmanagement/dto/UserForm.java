package com.nivuskorea.mealticketmanagement.dto;

import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "사원번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[0-9]+$", message = "사원번호는 숫자만 입력 가능합니다.")
    private String employeeNumber;

    private EmploymentStatus employmentStatus;


}
