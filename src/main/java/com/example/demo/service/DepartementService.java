package com.example.demo.service;

import com.example.demo.model.Departement;
import com.example.demo.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }

    public Optional<Departement> getDepartementById(Long id) {
        return departementRepository.findById(id);
    }

    public Departement saveDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    public void deleteDepartement(Long id) {
        departementRepository.deleteById(id);
    }
}
