package com.example.demo.controller;

import com.example.demo.dto.ModuleDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Module;
import com.example.demo.model.Option;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.OptionRepository;
import com.example.demo.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    // Get all modules
    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModule();
        return ResponseEntity.ok(modules);
    }
    @GetMapping("/options/{id}")
    public List<Module> getModulesByOption(@PathVariable Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found"));

        return moduleRepository.findByOptionId(option.getId());
    }


    // Add a new module
//    @PostMapping
//    public ResponseEntity<Module> addModule(@RequestBody Module module) {
//        Module savedModule = moduleService.addModule(module);
//        return ResponseEntity.ok(savedModule);
//    }
    @PostMapping
    public ResponseEntity<Module> addModule(@RequestBody ModuleDTO moduleDTO) {
        // Fetch the Option entity by ID
        Option option = optionRepository.findById(moduleDTO.getOption())
                .orElseThrow(() -> new IllegalArgumentException("Option not found with ID: " + moduleDTO.getOption()));

        // Create the Module object
        Module module = new Module();
        module.setNomModule(moduleDTO.getNomModule());
        module.setOption(option);

        // Save the Module to the database
        Module savedModule = moduleRepository.save(module);
        return ResponseEntity.ok(savedModule);
    }


    // Delete a module by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModuleById(@PathVariable Long id) {
        moduleService.deleteModuleById(id);
        return ResponseEntity.noContent().build();
    }

    // Update a module
    @PutMapping("/{id}")
    public ResponseEntity<Module> updateModule(
            @PathVariable Long id,
            @RequestBody ModuleDTO moduleDTO) {

        // Fetch the existing module from the database
        Module existingModule = moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with ID: " + id));

        // Update the module name
        existingModule.setNomModule(moduleDTO.getNomModule());

        // Fetch and update the related Option entity if provided
        if (moduleDTO.getOption() != null) {
            Option option = optionRepository.findById(moduleDTO.getOption())
                    .orElseThrow(() -> new ResourceNotFoundException("Option not found with ID: " + moduleDTO.getOption()));
            existingModule.setOption(option);
        }

        // Save the updated module
        Module savedModule = moduleRepository.save(existingModule);

        return ResponseEntity.ok(savedModule);
    }


    // Search for modules
    @GetMapping("/search")
    public ResponseEntity<List<Module>> searchModules(@RequestParam String query) {
        List<Module> searchResults = moduleService.searchModules(query);
        return ResponseEntity.ok(searchResults);
    }
}
