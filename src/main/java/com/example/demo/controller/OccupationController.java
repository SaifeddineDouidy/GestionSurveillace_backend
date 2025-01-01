package com.example.demo.controller;

import com.example.demo.model.Occupation;
import com.example.demo.service.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/occupations")
public class OccupationController {

    @Autowired
    private OccupationService occupationService;

    @GetMapping("/{enseignantId}/{sessionId}")
    public ResponseEntity<List<Occupation>> getOccupationsByEnseignantAndSession(
            @PathVariable Long enseignantId,
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(occupationService.getOccupationsByEnseignantAndSession(enseignantId, sessionId));
    }

    @GetMapping("/{sessionId}/date/{date}")
    public ResponseEntity<List<Occupation>> getOccupationsBySessionAndDate(
            @PathVariable Long sessionId,
            @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(occupationService.getOccupationsBySessionAndDate(sessionId, localDate));
    }

    @PostMapping
    public ResponseEntity<Occupation> createOccupation(@RequestBody Occupation occupation) {
        Occupation savedOccupation = occupationService.saveOccupation(occupation);
        return ResponseEntity.ok(savedOccupation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOccupation(@PathVariable Long id) {
        occupationService.deleteOccupation(id);
        return ResponseEntity.noContent().build();
    }
}
