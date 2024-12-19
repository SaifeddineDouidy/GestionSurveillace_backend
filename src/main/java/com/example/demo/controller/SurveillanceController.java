package com.example.demo.controller;

import com.example.demo.model.Exam;
import com.example.demo.service.SurveillanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surveillance")
public class SurveillanceController {

    @Autowired
    private SurveillanceService surveillanceService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Map<String, String>>> generateSurveillance(
            @RequestParam Long departmentId,
            @RequestBody List<Exam> exams) {

        Map<String, Map<String, String>> surveillanceTable = surveillanceService.generateSurveillanceTable(departmentId, exams);
        return ResponseEntity.ok(surveillanceTable);
    }
}
