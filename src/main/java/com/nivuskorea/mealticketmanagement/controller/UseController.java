package com.nivuskorea.mealticketmanagement.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UseController {
//    private final MealService mealService;

    @GetMapping("/use")
    public String use(Model model) {
        model.addAttribute("currentPage", "use");
//        model.addAttribute("users", mealService.getEmployedUsers());
        return "use";
    }

}
