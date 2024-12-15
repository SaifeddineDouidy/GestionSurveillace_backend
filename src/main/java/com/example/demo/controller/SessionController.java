package com.example.demo.controller;

import com.example.demo.model.Session;
import com.example.demo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        Session savedSession = sessionService.save(session);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.find(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        session.setId(id); // Ensure the ID matches
        sessionService.update(session);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Updated backend code to accept a JSON object
    @PutMapping("/{id}/validate")
    public ResponseEntity<Session> toggleValidation(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean isValid = body.get("isValid");  // Extract the value from the request body
        Session session = sessionService.find(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        session.setValid(isValid);
        sessionService.update(session);
        return ResponseEntity.ok(session);
    }

}
