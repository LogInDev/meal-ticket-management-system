package com.nivuskorea.mealticketmanagement.controller;
import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.dto.UserForm;
import com.nivuskorea.mealticketmanagement.service.UserService;
import com.nivuskorea.mealticketmanagement.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UseController {

    private final UserService userService;

    /**
     * 중식화면에 출력할 식권대장 명단
     * @param mealType
     * @param model
     * @return
     */
    @GetMapping("/meal_use")
    public String use(@RequestParam(name = "mealType", defaultValue = "lunch") String mealType,
                      @ModelAttribute("userForm") UserForm userForm, // FlashAttribute 받음
                      Model model) {
        model.addAttribute("currentPage", "use");
        model.addAttribute("mealType", mealType);
        model.addAttribute("users",userService.getEligibleUsersForTodayMeal(MealStatus.from(mealType)));
        // FlashAttribute에서 받은 userForm이 없으면 기본값 설정
        if (userForm.getEmploymentStatus() == null) {
            userForm.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        }
        model.addAttribute("userForm", userForm);

        return "use";
    }

    @PostMapping("/meal_use")
    public String saveOrUpdateUser(@Validated(ValidationSequence.class) UserForm userForm,
                                   BindingResult result,
                                   @RequestParam(name = "mealType", defaultValue = "lunch") String mealType,
                                   Model model, RedirectAttributes redirectAttributes) {
        // 값 입력 안했을 경우
        if(result.hasErrors()) {
            System.out.println("유효성 검증 실패" + mealType);
            model.addAttribute("currentPage", "use");
            model.addAttribute("mealType", mealType);
            model.addAttribute("users", userService.getEligibleUsersForTodayMeal(MealStatus.from(mealType)));
            redirectAttributes.addFlashAttribute("message", "수정이 적용되지 않았습니다.");
            return "use";
        }

        if (userService.isDuplicateUser(userForm)) { // 중복된 사원번호가 있고 수정된 부분이 없다.
            redirectAttributes.addFlashAttribute("message", "이미 있는 사용자입니다.");
            redirectAttributes.addFlashAttribute("userForm", userForm);
        }else{  // 중복된 사원번호가 있지만 수정이 필요하거나 중복된 사원번호가 없어서 새로 추가가 필요한 경우
            // User 객체를 업데이트 혹은 추가해서  DB에 반영
            userService.saveOrUpdateUser(userForm);
            redirectAttributes.addFlashAttribute("message", "수정 완료했습니다.");
        }


        return "redirect:/meal_use?mealType=" + mealType;
    }

    /**
     * String 타입 필드 공백 제거
     * @param binder Spring MVC에서 컨트롤러의 요청 파라미터를 바인딩할 때 사용되는 객체.
     *               즉, 클라이언트가 보낸 데이터를 컨트롤러의 파라미터나 객체에 자동으로 넣어주는 역할
     */
    @InitBinder("userForm")
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true) {
            @Override
            public void setAsText(String text) {
                super.setAsText(text == null ? null : text.replaceAll("\\s+", ""));
            }
        });
    }

}
