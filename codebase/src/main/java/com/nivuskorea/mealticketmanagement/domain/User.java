package com.nivuskorea.mealticketmanagement.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private Integer employeeNumber;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @OneToMany(mappedBy = "user")
    private List<MealRecord> mealRecords = new ArrayList<>();

    protected User() {
        //JPA가 사용하는 기본 생성자
    }


    public User(String name, Integer employeeNumber, EmploymentStatus employmentStatus) {
        this.name = name;
        this.employeeNumber = employeeNumber;
        this.employmentStatus = employmentStatus;
    }

    /**
     * User 정보 수정을 위한 메서드
     * @param name 수정할 이름
     * @param employmentStatus 수정할 재직 상태
     */
    public void update(String name, EmploymentStatus employmentStatus) {
        this.name = name;
        this.employmentStatus = employmentStatus;
    }

}
