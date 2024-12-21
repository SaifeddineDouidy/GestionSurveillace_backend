package com.example.demo.service;


import com.example.demo.model.Option;
import com.example.demo.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Module;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepository;

    public List<Module> getAllModule() {
        return moduleRepository.findAll();
    }

    public Module addModule(Module module) {
        return moduleRepository.save(module);
    }

    public void deleteModule(Module module) {
        moduleRepository.delete(module);
    }

    public void deleteModuleById(Long id) {
        moduleRepository.deleteById(id);
    }

    public Module updateModule(Module module) {
        return moduleRepository.save(module);
    }

    public List<Module> searchModules(String query) {
        return moduleRepository.findAll().stream()
                .filter(local -> local.getNomModule().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }


    public List<Module> getModulesByOption(Long optionId) {
        return moduleRepository.findByOptionId(optionId);
    }

    public boolean existsByNomModuleAndOption(String nomModule, com.example.demo.model.Option option) {
        return moduleRepository.existsByNomModuleAndOption(nomModule, option);
    }
}
