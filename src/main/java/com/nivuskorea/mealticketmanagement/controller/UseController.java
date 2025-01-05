package com.nivuskorea.mealticketmanagement.controller;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import com.nivuskorea.mealticketmanagement.service.MealRecordService;
import com.nivuskorea.mealticketmanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UseController {

    private final MealRecordService mealRecordService;
    private final UserService userService;

    /**
     * 중식화면에 출력할 식권대장 명단
     * @param mealType
     * @param model
     * @return
     */
    @GetMapping("/use")
    public String use(@RequestParam(name = "mealType", defaultValue = "lunch") String mealType,
                      Model model) {
        model.addAttribute("currentPage", "use");
        model.addAttribute("mealType", mealType);
        model.addAttribute("users",mealRecordService.getLunchRecord(MealStatus.from(mealType)));
        model.addAttribute("userForm", new UserForm());

        return "use";
    }

    @PostMapping("/use")
    public String addUser(@Valid UserForm userForm,
                          BindingResult result,
                          @RequestParam(name = "mealType", defaultValue = "lunch") String mealType,
                          Model model) {
        if (userService.isDuplicateEmployeeNumber(userForm.getEmployeeNumber())) {
            result.rejectValue("employeeNumber", "duplicate", "중복된 사원번호가 있습니다.");
        }

        // 값 입력 안했을 경우
        if(result.hasErrors()) {
            System.out.println("유효성 검증 실패" + mealType);
            model.addAttribute("currentPage", "use");
            model.addAttribute("mealType", mealType);
            model.addAttribute("users", mealRecordService.getLunchRecord(MealStatus.from(mealType)));

            return "use";
        }

        // 유효성 검증 성공 처리
        userService.addUser(new User(userForm.getName(), userForm.getEmployeeNumber(), userForm.getEmploymentStatus()));
        return "redirect:/use?mealType=" + mealType;
    }



}
