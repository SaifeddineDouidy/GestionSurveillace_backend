package com.example.demo.controller;

import com.example.demo.model.Local;
import com.example.demo.service.LocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locaux")
public class LocalController {

    @Autowired
    private LocalService localService;

    // Get all locaux
    @GetMapping
    public ResponseEntity<List<Local>> getAllLocaux() {
        return ResponseEntity.ok(localService.getAllLocaux());
    }

    // Add a new local
    @PostMapping
    public ResponseEntity<Local> addLocal(@RequestBody Local local) {
        Local createdLocal = localService.addLocal(local);
        return ResponseEntity.ok(createdLocal);
    }

    // Update an existing local
    @PutMapping("/{id}")
    public ResponseEntity<Local> updateLocal(
            @PathVariable Long id,
            @RequestBody Local updatedLocal) {
        updatedLocal.setId(id);
        Local local = localService.updateLocal(updatedLocal);
        return ResponseEntity.ok(local);
    }

    // Delete a local
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        localService.deleteLocalById(id);
        return ResponseEntity.noContent().build();
    }

    // Filter locaux (optional query parameter)
    @GetMapping("/search")
    public ResponseEntity<List<Local>> searchLocaux(@RequestParam String query) {
        List<Local> filteredLocaux = localService.searchLocaux(query);
        return ResponseEntity.ok(filteredLocaux);
    }
}
