package com.example.demo.service;

import com.example.demo.dto.ExamDTO;
import com.example.demo.model.*;
import com.example.demo.model.Module;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private LocalRepository localRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LocalService localService;
    @Autowired
    private OptionRepository optionRepository;

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    public int countExams() {
        return (int) examRepository.count(); // Assuming `count()` is defined in `JpaRepository`
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }


    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        examRepository.delete(exam);
    }


    public List<Map<String, Object>> getRecentExams() {
        return examRepository.findTop5ByOrderByDateDescStartTimeDesc().stream()
                .map(exam -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", exam.getId());
                    map.put("date", exam.getDate());
                    map.put("startTime", exam.getStartTime());
                    map.put("endTime", exam.getEndTime());
                    map.put("departement", exam.getDepartement().getDepartmentName());
                    map.put("enseignant", exam.getEnseignant().getName());
                    map.put("option", exam.getOption().getNomDeFiliere());
                    map.put("module", exam.getModule().getNomModule());
                    return map;
                })
                .collect(Collectors.toList());
    }


    private void collect(Collector<Object,?, List<Object>> list) {
    }

    public List<Exam> findExamsByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return examRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime);
    }
}
