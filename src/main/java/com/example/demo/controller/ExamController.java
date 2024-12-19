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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody ExamDTO examDTO) {
        // Fetch related entities
        Departement departement = departementRepository.findById(examDTO.getDepartement())
                .orElseThrow(() -> new RuntimeException("Departement not found"));
        Enseignant enseignant = enseignantRepository.findById(examDTO.getEnseignant())
                .orElseThrow(() -> new RuntimeException("Enseignant not found"));
        Option option = optionRepository.findById(examDTO.getOption())
                .orElseThrow(() -> new RuntimeException("Option not found"));
        Module module = moduleRepository.findById(examDTO.getModule())
                .orElseThrow(() -> new RuntimeException("Module not found"));

        // Fetch locaux and assign them to the exam
        List<Local> locaux = localService.getLocauxByIds(examDTO.getLocauxIds());

        // Create and save the exam
        Exam exam = new Exam();
        exam.setDate(examDTO.getDate());
        exam.setStartTime(examDTO.getStartTime());
        exam.setEndTime(examDTO.getEndTime());
        exam.setDepartement(departement);
        exam.setEnseignant(enseignant);
        exam.setOption(option);
        exam.setModule(module);
        exam.setLocaux(locaux);

        Exam savedExam = examService.createExam(exam);
        return ResponseEntity.ok(savedExam);
    }


    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        return ResponseEntity.ok(examService.getAllExams());
    }

}
