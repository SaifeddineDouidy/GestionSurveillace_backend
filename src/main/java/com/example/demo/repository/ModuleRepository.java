package com.example.demo.repository;

import com.example.demo.model.Module;
import com.example.demo.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository  extends JpaRepository<Module, Long> {
    List<Module> findByOptionId(Long optionId);
    boolean existsByNomModuleAndOption(String nomModule, Option option);

}
