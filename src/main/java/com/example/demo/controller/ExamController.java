package com.example.demo.controller;

import com.example.demo.dto.ExamDTO;
import com.example.demo.model.*;
import com.example.demo.model.Module;
import com.example.demo.repository.DepartementRepository;
import com.example.demo.repository.EnseignantRepository;
import com.example.demo.repository.ModuleRepository;
import com.example.demo.repository.OptionRepository;
import com.example.demo.service.ExamService;
import com.example.demo.service.LocalService;
import com.example.demo.service.SurveillanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LocalService localService;
    @Autowired
    private SurveillanceService surveillanceService;

    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody ExamDTO examDTO) {
        Exam exam = examService.createExam(examDTO);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Exam>> getExamsByDateAndTime(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        List<Exam> exams = examService.findExamsByDateAndTime(date, startTime, endTime);
        return ResponseEntity.ok(exams);
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable Long id, @RequestBody ExamDTO examDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Exam>> getExamsBySession(@PathVariable Long sessionId) {
        List<Exam> exams = examService.findBySessionId(sessionId);
        return ResponseEntity.ok(exams);
    }
}
