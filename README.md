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