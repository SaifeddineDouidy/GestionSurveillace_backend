package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Departement;
import com.example.demo.model.Enseignant;
import com.example.demo.model.Option;
import com.example.demo.service.DepartementService;
import com.example.demo.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;
    @Autowired
    private OptionService optionService;

    @GetMapping
    public List<Departement> getAllDepartements() {
        return departementService.getAllDepartements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Long id) {
        return departementService.getDepartementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<List<Departement>> createDepartments(@RequestBody List<Departement> departments) {
        List<Departement> savedDepartments = departementService.saveAll(departments);
        return ResponseEntity.ok(savedDepartments);
    }

    @PostMapping
    public Departement createDepartement(@RequestBody Departement departement) {
        return departementService.saveDepartement(departement);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Departement> updateDepartement(@PathVariable Long id, @RequestBody Departement departementDetails) {
        return departementService.getDepartementById(id).map(departement -> {
            // Update the department name
            if (departementDetails.getDepartmentName() != null) {
                departement.setDepartmentName(departementDetails.getDepartmentName());
            }

                // Update the parent reference in enseignants
                for (Enseignant enseignant : departement.getEnseignants()) {
                    enseignant.setDepartment(departement);
                }

            Departement updatedDepartement = departementService.saveDepartement(departement);
            return ResponseEntity.ok(updatedDepartement);
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        if (departementService.getDepartementById(id).isPresent()) {
            departementService.deleteDepartement(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/enseignants")
    public ResponseEntity<List<Enseignant>> getEnseignantsByDepartement(@PathVariable Long id) {
        try {
            List<Enseignant> enseignants = departementService.getEnseignantsByDepartement(id);
            return ResponseEntity.ok(enseignants);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/options")
    public ResponseEntity<List<Option>> getOptionsByDepartment(@PathVariable Long id) {
        List<Option> options = optionService.getOptionsByDepartment(id);
        return ResponseEntity.ok(options);
    }

}
