package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.demo.service.ExamService;
import com.example.demo.service.EnseignantService;
import com.example.demo.service.DepartementService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ExamService examService;

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private DepartementService departementService;

    // 1. Get Stats
    @GetMapping("/stats")
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("exams", examService.countExams());
        stats.put("enseignants", enseignantService.countEnseignants());
        stats.put("departments", departementService.countDepartments());
        return stats;
    }

    // 2. Get Department Distribution
    @GetMapping("/department-distribution")
    public Map<String, Object> getDepartmentDistribution() {
        List<String> departmentNames = departementService.getDepartmentNames();
        List<Integer> enseignantCounts = departementService.getEnseignantCountsPerDepartment();

        Map<String, Object> data = new HashMap<>();
        data.put("labels", departmentNames);
        data.put("values", enseignantCounts);
        return data;
    }

    // 3. Get Recent Exams
    @GetMapping("/recent-exams")
    public List<Map<String, Object>> getRecentExams() {
        return examService.getRecentExams();
    }
}
