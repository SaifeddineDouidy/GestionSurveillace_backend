package com.example.demo.repository;

import com.example.demo.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    // You can add custom queries if needed, e.g., find sessions by type
}
