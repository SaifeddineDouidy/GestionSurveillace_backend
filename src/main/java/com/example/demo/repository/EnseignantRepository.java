package com.example.demo.repository;

import com.example.demo.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    List<Enseignant> findByDepartmentIdAndDispenseFalse(Long departmentId);
    Optional<Enseignant> findByName(String name);

}
