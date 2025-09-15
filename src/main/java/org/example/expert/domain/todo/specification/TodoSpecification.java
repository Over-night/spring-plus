package org.example.expert.domain.todo.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TodoSpecification {
    public static Specification<Todo> weatherEqualsIgnoreCase(String value) {
        if (value == null || value.isBlank()) return null;
        return (root, query, cb) ->
                cb.equal(cb.lower(root.get("weather")), value.toLowerCase());
    }

    public static Specification<Todo> modifiedAtFrom(LocalDateTime from) {
        if (from == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("modifiedAt"), from);
    }

    public static Specification<Todo> modifiedAtTo(LocalDateTime to) {
        if (to == null) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("modifiedAt"), to);
    }

    public static Specification<Todo> modifiedAtBetween(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null) {
            return (root, query, cb) -> cb.between(root.get("modifiedAt"), from, to);
        }
        // 하나만 있으면 해당 경계 조건으로 대체
        return Specification.where(modifiedAtFrom(from)).and(modifiedAtTo(to));
    }

}
