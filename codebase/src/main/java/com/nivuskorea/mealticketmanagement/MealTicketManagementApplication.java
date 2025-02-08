package com.nivuskorea.mealticketmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MealTicketManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealTicketManagementApplication.class, args);
    }

}
