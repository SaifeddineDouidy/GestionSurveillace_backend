package com.example.demo.controller;

import com.example.demo.model.Local;
import com.example.demo.model.Option;
import com.example.demo.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    // Get all options
    @GetMapping
    public ResponseEntity<List<Option>> getAllOptions() {
        List<Option> options = optionService.getAllOptions();
        return ResponseEntity.ok(options);
    }

    // Add a new option
    @PostMapping
    public ResponseEntity<Option> addOption(@RequestBody Option option) {
        Option savedOption = optionService.addOptions(option);
        return ResponseEntity.ok(savedOption);
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



}
