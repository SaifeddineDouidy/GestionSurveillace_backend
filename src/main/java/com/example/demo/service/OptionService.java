package com.example.demo.service;

import com.example.demo.model.Departement;
import com.example.demo.model.Local;
import com.example.demo.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.model.Option;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OptionService {
    @Autowired
    private OptionRepository optionRepository;


    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    public Option addOptions(Option option) {
        return optionRepository.save(option);
    }

    public void deleteOptions(Option option) {
        optionRepository.delete(option);
    }

    public void deleteOptionsById(Long id) {
        optionRepository.deleteById(id);
    }

    public Option updateOptions(Option option) {
        return optionRepository.save(option);
    }

    public List<Option> searchOptions(String query) {
        return optionRepository.findAll().stream()
                .filter(local -> local.getNomDeFiliere().toLowerCase().contains(query.toLowerCase())
                        || String.valueOf(local.getAnnee()).contains(query))
                .collect(Collectors.toList());
    }
    public List<Option> getOptionsByDepartment(Long departmentId) {
        return optionRepository.findByDepartementId(departmentId);
    }
    public Option findByNomDeFiliereAndDepartement(String nomDeFiliere, Departement departement) {
        return optionRepository.findByNomDeFiliereAndDepartement(nomDeFiliere,departement);
    }

}
