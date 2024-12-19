package com.example.demo.controller;

import com.example.demo.model.Departement;
import com.example.demo.model.Local;
import com.example.demo.model.Module;
import com.example.demo.model.Option;
import com.example.demo.service.DepartementService;
import com.example.demo.service.ModuleService;
import com.example.demo.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    @Autowired
    private OptionService optionService;
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private DepartementService departementService;
    // Get all options
    @GetMapping
    public ResponseEntity<List<Option>> getAllOptions() {
        List<Option> options = optionService.getAllOptions();
        return ResponseEntity.ok(options);
    }

    // Add a new option
    @PostMapping
    public ResponseEntity<Option> addOption(@RequestBody Option option) {
        System.out.println("Received Option: " + option);
        System.out.println("Department ID: " + option.getDepartement().getId());

        if (option.getDepartement() == null || option.getDepartement().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Handle null departement or id
        }

        // Fetch the Departement by ID
        Long departementId = option.getDepartement().getId();
        Optional<Departement> departement = departementService.getDepartementById(departementId);

        if (!departement.isPresent()) {
            return ResponseEntity.notFound().build(); // If Departement is not found, return 404
        }

        // Set the Departement in the Option
        option.setDepartement(departement.get());

        // Save the Option
        Option savedOption = optionService.addOptions(option);
        return ResponseEntity.ok(savedOption); // Return the saved Option
    }


    // Delete an option by object
    @DeleteMapping
    public ResponseEntity<Void> deleteOption(@RequestBody Option option) {
        optionService.deleteOptions(option);
        return ResponseEntity.noContent().build();
    }

    // Delete an option by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOptionById(@PathVariable Long id) {
        optionService.deleteOptionsById(id);
        return ResponseEntity.noContent().build();
    }

    // Update an existing option
    @PutMapping("/{id}")
    public ResponseEntity<Option> updateOption(@PathVariable Long id,@RequestBody Option option) {
        option.setId(id);
        Option updatedOption = optionService.updateOptions(option);
        return ResponseEntity.ok(updatedOption);
    }

    // Search for options by query
    @GetMapping("/search")
    public ResponseEntity<List<Option>> searchOptions(@RequestParam("query") String query) {
        List<Option> result = optionService.searchOptions(query);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/departement/{id}")
    public ResponseEntity<List<Option>> getOptionsByDepartment(@PathVariable Long id) {
        List<Option> options = optionService.getOptionsByDepartment(id);
        return ResponseEntity.ok(options);
    }


    @GetMapping("/{id}/modules")
    public ResponseEntity<List<Module>> getModulesByOption(@PathVariable Long id) {
        List<Module> modules = moduleService.getModulesByOption(id);
        return ResponseEntity.ok(modules);
    }

}
