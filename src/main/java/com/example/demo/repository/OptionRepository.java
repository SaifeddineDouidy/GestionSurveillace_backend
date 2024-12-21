package com.example.demo.repository;

import com.example.demo.model.Departement;
import com.example.demo.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository  extends JpaRepository<Option, Long> {
    List<Option> findByDepartementId(Long departementId);
    Option findByNomDeFiliereAndDepartement(String nomDeFiliere, Departement departement);


}
