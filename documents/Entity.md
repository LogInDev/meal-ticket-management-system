# `@MappedSuperclass` vs `@Embeddable`

## `@MappedSuperclass`
- 상속 기반 재사용
	- 상속을 통해 공통 속성 및 매핑 정보를 하위 엔티티에서 재사용할 때 사용
- 테이블 매핑 X
	- 데이터베이스에 직접 테이블로 생성되지 않음
	- 이를 상속받는 엔티티만 실제 테이블로 매핑됨.
- 엔티티의 공통 필드 정의:
	- `id`, `createdAt`, `updatedAt`과 같은 공통 정의에 적합
- 예제
	```java
	
```


# `@PrePersist`, `@PreUpdate` vs `@EntityListeners`

## `@PrePersist`, `@PreUpdate`
**[특징]**
- 엔티티 클래스 내에 콜백 메서드를 정의하여 생명주기 이벤트를 처리
- `@PrePersist` : 엔티티가 처음 저장되기 전에 호출
- `@PreUpdate` : 엔티티가 업데이트되기 전에 호출

**[예제]**
```java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
```

**[장점]**
1. 간단하고 바로 사용 가능
2. 엔티티 클래스 안에 로직이 포함되어 관리가 쉬움












































































































































































































































































































