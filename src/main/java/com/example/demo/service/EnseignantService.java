package com.example.demo.service;

import com.example.demo.model.Enseignant;
import com.example.demo.repository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public List<Enseignant> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    public Optional<Enseignant> getEnseignantById(Long id) {
        return enseignantRepository.findById(id);
    }

    public Enseignant saveEnseignant(Enseignant enseignant) {
        return enseignantRepository.save(enseignant);
    }

    public void deleteEnseignant(Long id) {
        enseignantRepository.deleteById(id);
    }
    public int countEnseignants() {
        return (int) enseignantRepository.count();
    }
    public List<Enseignant> saveAll(List<Enseignant> enseignants) {
        return enseignantRepository.saveAll(enseignants);
    }
    public Enseignant getEnseignantByName(String name) {
        return enseignantRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Enseignant not found with name: " + name));
    }
}
