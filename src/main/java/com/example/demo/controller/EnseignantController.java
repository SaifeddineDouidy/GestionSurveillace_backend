package com.example.demo.controller;

import com.example.demo.model.Enseignant;
import com.example.demo.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
public class EnseignantController {

    @Autowired
    private EnseignantService enseignantService;


    @PostMapping("/bulk-create")
    public ResponseEntity<List<Enseignant>> createEnseignants(@RequestBody List<Enseignant> enseignants) {
        List<Enseignant> savedEnseignants = enseignantService.saveAll(enseignants);
        return ResponseEntity.ok(savedEnseignants);
    }

    @GetMapping
    public List<Enseignant> getAllEnseignants() {
        return enseignantService.getAllEnseignants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enseignant> getEnseignantById(@PathVariable Long id) {
        return enseignantService.getEnseignantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Enseignant createEnseignant(@RequestBody Enseignant enseignant) {
        return enseignantService.saveEnseignant(enseignant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enseignant> updateEnseignant(@PathVariable Long id, @RequestBody Enseignant enseignantDetails) {
        return enseignantService.getEnseignantById(id).map(enseignant -> {
            enseignant.setName(enseignantDetails.getName());
            enseignant.setEmail(enseignantDetails.getEmail());
            enseignant.setDispense(enseignantDetails.isDispense());
            enseignant.setDepartment(enseignantDetails.getDepartment());
            Enseignant updatedEnseignant = enseignantService.saveEnseignant(enseignant);
            return ResponseEntity.ok(updatedEnseignant);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable Long id) {
        if (enseignantService.getEnseignantById(id).isPresent()) {
            enseignantService.deleteEnseignant(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
