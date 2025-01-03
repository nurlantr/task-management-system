package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments WHERE t.author.id = :authorId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority)")
    Page<Task> findByAuthorIdWithFilters(Long authorId, String status,
                                         String priority, Pageable pageable);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments WHERE t.executor.id = :executorId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority)")
    Page<Task> findByExecutorIdWithFilters(Long executorId, String status,
                                           String priority, Pageable pageable);
}
