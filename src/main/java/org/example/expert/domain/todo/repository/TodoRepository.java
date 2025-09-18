package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends
        JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo>,
        TodoQueryDSLRepository {
    @EntityGraph(attributePaths = "user") // t.user를 fetch
    Page<Todo> findAll(Specification<Todo> spec, Pageable pageable);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query(value = """
            SELECT t
            FROM Todo t
            LEFT JOIN FETCH t.user u
            WHERE (:weather IS NULL OR LOWER(t.weather) = LOWER(:weather))
                AND (:from IS NULL OR t.modifiedAt >= :from)
                AND (:to IS NULL OR t.modifiedAt <= :to)
            ORDER BY t.modifiedAt DESC
    """, countQuery = """
            SELECT COUNT(t)
            FROM Todo t
            WHERE (:weather IS NULL OR LOWER(t.weather) = LOWER(:weather))
                AND (:from IS NULL OR t.modifiedAt >= :from)
                AND (:to   IS NULL OR t.modifiedAt <= :to)
    """)
    Page<Todo> findByWeatherAndModifiedAt(
            @Param("weather")   String weather,
            @Param("from")      LocalDateTime from,
            @Param("to")        LocalDateTime to,
            Pageable pageable
    );

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN t.user " +
//            "WHERE t.id = :todoId")
//    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
