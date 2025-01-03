package com.nivuskorea.mealticketmanagement.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
//    private final MealService mealService;

    @GetMapping("/lunch")
    public String lunch(Model model) {
//        model.addAttribute("users", mealService.getEmployedUsers());
        return "lunch";
    }
}
