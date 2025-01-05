package com.nivuskorea.mealticketmanagement.api;

import com.nivuskorea.mealticketmanagement.domain.MealStatus;
import com.nivuskorea.mealticketmanagement.dto.MealRecordRequest;
import com.nivuskorea.mealticketmanagement.service.MealRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mealRecord")
@RequiredArgsConstructor
public class RecordApiController {

    private final MealRecordService mealRecordService;

    @PostMapping("/updateRecord")
    public ResponseEntity<String> updateRecord(@RequestBody MealRecordRequest request) {
        try {
            mealRecordService.addRecord(request.getUserId(),
                    MealStatus.from(request.getMealType()),
                    request.getIsCanceled());
            return ResponseEntity.ok("Record successfully updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating record");
        }
    }
}
