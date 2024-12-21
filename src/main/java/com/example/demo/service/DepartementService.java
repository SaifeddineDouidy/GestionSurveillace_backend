package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Departement;
import com.example.demo.model.Enseignant;
import com.example.demo.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;


    public List<Departement> saveAll(List<Departement> departments) {
        return departementRepository.saveAll(departments);
    }

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

    public List<Enseignant> getEnseignantsByDepartement(Long departementId) {
        return departementRepository.findById(departementId)
                .map(Departement::getEnseignants)
                .orElseThrow(() -> new ResourceNotFoundException("Departement not found with id: " + departementId));
    }

    public int countDepartments() {
        return (int) departementRepository.count();
    }

    public List<String> getDepartmentNames() {
        return departementRepository.findAll().stream()
                .map(Departement::getDepartmentName)
                .collect(Collectors.toList());
    }

    public List<Integer> getEnseignantCountsPerDepartment() {
        return departementRepository.findAll().stream()
                .map(dept -> dept.getEnseignants().size())
                .collect(Collectors.toList());
    }

    public Optional<Departement> findByName(String departmentName) {
        return departementRepository.findByDepartmentName(departmentName);
    }




    public Optional<Departement> findByDepartmentName(String name) {
        return departementRepository.findByDepartmentName(name);
    }
}
