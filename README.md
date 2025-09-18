# SPRING PLUS

## 풀이

---
### 1번문제 
**1. 코드 개선 퀴즈 - @Transactional의 이해**

```TodoService```클래스의 ```saveTodo``` 메소드에 ```@Transaction``` 어노테이션 지정
- ```@Transaction(readonly=true)``` 어노테이션이 클래스에 전역 설정됨
- ```readonly```가 쓰기 작업을 해야하는 ```saveTodo``` 메소드까지 적용되었음
- -> 따라서 해당 메소드에 ```@Transaction``` 어노테이션을 세부 지정 (default가 ```false```이므로 생략가능)

**2. 코드 추가 퀴즈 - JWT의 이해**

```nickname``` 컬럼 추가 및 매개변수 반영
- ```User``` ```AuthUser``` 클래수에 맴버변수 추가
- config에서 ```nickname```을 추가로 수신하도록 구문 수정 및 추가
- ```SignupRequest``` dto 및 Service 메소드에 관련 구문 추가


**3. 코드 개선 퀴즈 -  JPA의 이해**

```Specification``` 적용을 통한 검색 필터링 기능 지원
- ```weather``` ```from``` ```to``` 쿼리파라미터 조회 및 Specification에 반영
- Repository에서 ```JpaSpecificationExecutor```를 상속받도록 구현
- JPQL을 통한 필터링 기능 또한 코드 상 구현 (```findByWeatherAndModifiedAt```)

**4. 테스트 코드 퀴즈 - 컨트롤러 테스트의 이해**
[UserAdminController.java](src/main/java/org/example/expert/domain/user/controller/UserAdminController.java)
```invalidRequestExceptionException``` 에 대응하여 테스트 수정
- ```invalidRequestExceptionException```은 ```400 BAD REQUEST```을 반환하도록 설계됨
- ```getTodo``` 메소드는 찾으려는 ID가 없을 경우 위 예외를 반환하도록 설계됨
- ```200 OK```로 예측하는 구문을 ```400 BAD REQUEST```으로 예측하도록 변경

**5. 코드 개선 퀴즈 - AOP의 이해**

```@After``` 어노테이션 수정
- 설계 상 메소드가 실행 전 해당 코드가 동작해야하나 ```@After```은 잘못된 순서 지정
- 명세서 상 ```UserAdminController.changeUserRole()``` 메서드를 대상으로 해야하나 ```UserController.getUser()```를 대상으로 함
- 관련 이슈를 설계 의도에 맞도록 어노테이션 및 메소드명 수정

### 2번문제
**6. JPA Cascade**

```JPA Cascade```의 ```CascadeType.PERSIST``` 속성 이용
- 부모 엔티티가 저장될때, 연관된 자식 엔티티도 함께 저장되도록 함
- 해당 속성을 지정해 todo가 저장될 때 todo의 manager도 함께 저장되로록 설계

**7. N+1**

```JOIN```만 사용 시 연관 엔티티가 즉시 로딩되지 않음
- ```user``` 필드의 Fetch Type가 ```LAZY```일 경우 프록시 객체로 남고, ```comment.getUser()``` 사용 시 추가쿼리 발생
- ```EntityGraph``` 어노테이션을 를 사용해 ```user```을 즉시 로딩
- ```Fetch Join```을 활용하여 Comment와 User 데이터를 한번에 가져오는 방안도 있음

**8. QueryDSL**

```QueryDSL```패키지를 gradle에 추가 후 코드에 적용
- ```JPAQueryFactory``` 컨피그 설정으로 엔티티 메니저에 쿼리를 날림
- ```TodoQueryDSLRepository``` 인터페이스와 Implement 클래스로 ```findByIdWithUser``` 메소드를 ```QueryDSL```로 구현
- ```TodoRepository```가 ```TodoQueryDSLRepository```을 상속받아 QueryDSL 적용

