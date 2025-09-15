# SPRING PLUS

## 풀이

---
### 1번문제 
**답안**

```TodoService```클래스의 ```saveTodo``` 메소드에 ```@Transaction``` 어노테이션 지정 

**풀이**
- ```@Transaction(readonly=true)``` 어노테이션이 클래스에 전역 설정됨
- ```readonly```가 쓰기 작업을 해야하는 ```saveTodo``` 메소드까지 적용되었음
- -> 따라서 해당 메소드에 ```@Transaction``` 어노테이션을 세부 지정 (default가 ```false```이므로 생략가능)