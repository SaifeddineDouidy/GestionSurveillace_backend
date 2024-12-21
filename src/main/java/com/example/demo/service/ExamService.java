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
    private SessionRepository sessionRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LocalService localService;
    @Autowired
    private OptionRepository optionRepository;

    public Exam createExam(ExamDTO examDTO) {
        // Fetch the session
        Session session = sessionRepository.findById(examDTO.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        // Fetch related entities
        Exam exam = new Exam();
        exam.setDate(examDTO.getDate());
        exam.setStartTime(examDTO.getStartTime());
        exam.setEndTime(examDTO.getEndTime());
        exam.setDepartement(departementRepository.findById(examDTO.getDepartement()).orElseThrow());
        exam.setEnseignant(enseignantRepository.findById(examDTO.getEnseignant()).orElseThrow());
        exam.setOption(optionRepository.findById(examDTO.getOption()).orElseThrow());
        exam.setModule(moduleRepository.findById(examDTO.getModule()).orElseThrow());
        exam.setSession(session); // Link the exam to the session

        // Fetch and associate locaux
        List<Local> locaux = localService.getLocauxByIds(examDTO.getLocauxIds());
        exam.setLocaux(locaux);
        for (Local local : locaux) {
            local.setExam(exam); // Link the local to the exam
            local.setDisponible(false); // Mark the local as unavailable
        }

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

    public List<Exam> findBySessionId(Long sessionId) {
        return examRepository.findBySessionId(sessionId);
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
