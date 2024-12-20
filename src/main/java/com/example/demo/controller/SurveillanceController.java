package com.example.demo.controller;

import com.example.demo.service.SurveillanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/surveillance")
public class SurveillanceController {

    @Autowired
    private SurveillanceService surveillanceService;

    @GetMapping("/generate")
    public ResponseEntity<Map<String, Map<String, String>>> generateSurveillanceTable(@RequestParam Long sessionId) {
        Map<String, Map<String, String>> surveillanceTable = surveillanceService.generateSurveillanceTable(sessionId);
        return ResponseEntity.ok(surveillanceTable);
    }
}