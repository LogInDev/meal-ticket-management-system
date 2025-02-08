package com.nivuskorea.mealticketmanagement.controller;
import com.nivuskorea.mealticketmanagement.domain.EmploymentStatus;
import com.nivuskorea.mealticketmanagement.domain.MealRecord;
import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecordController {

    @GetMapping("/records")
    public String records(Model model) {
        model.addAttribute("currentPage", "records");
        return "records";
    }

}
