package com.nivuskorea.mealticketmanagement.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecordController {
//    private final MealService mealService;

    @GetMapping("/records")
    public String records(Model model) {
        model.addAttribute("currentPage", "records");
//        model.addAttribute("users", mealService.getEmployedUsers());
        return "records";
    }

}
