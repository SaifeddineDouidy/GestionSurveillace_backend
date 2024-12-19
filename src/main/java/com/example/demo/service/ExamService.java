package com.example.demo.service;

import com.example.demo.model.Exam;
import com.example.demo.repository.ExamRepository;
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

    public Exam createExam(Exam exam) {
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
