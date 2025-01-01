package com.example.demo.repository;

import com.example.demo.model.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {
    List<Occupation> findByEnseignantIdAndSessionId(Long enseignantId, Long sessionId);
    List<Occupation> findBySessionIdAndDate(Long sessionId, LocalDate date);
    List<Occupation> findByEnseignantIdAndDate(Long enseignantId, LocalDate date);

}
