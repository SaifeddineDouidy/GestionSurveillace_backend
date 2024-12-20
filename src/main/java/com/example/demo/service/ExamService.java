package com.example.demo.service;

import com.example.demo.dto.ExamDTO;
import com.example.demo.model.Exam;
import com.example.demo.model.Local;
import com.example.demo.model.Session;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired

    private OptionRepository optionRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<Exam> findBySessionId(Long sessionId) {
        return examRepository.findBySessionId(sessionId);
    }




    @Autowired
    private LocalService localService;

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
    public List<Map<String, Object>> getRecentExams() {
        // Example query, replace with actual database logic
        return List.of(
                Map.of("name", "Math", "score", 90),
                Map.of("name", "Physics", "score", 85)
        );
    }

    public List<Exam> findExamsByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        return examRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime);
    }
}
