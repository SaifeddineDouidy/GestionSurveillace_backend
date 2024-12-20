package com.example.demo.service;
import com.example.demo.model.Enseignant;
import com.example.demo.model.*;
import com.example.demo.repository.EnseignantRepository;
import com.example.demo.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SurveillanceService {

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private LocalService localService;

    @Autowired
    private ExamService examService;

    public Map<String, Map<String, String>> generateSurveillanceTable() {
        List<Exam> exams = examService.getAllExams();
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        Map<String, Integer> professorAssignments = new HashMap<>();
        Map<String, Map<String, String>> surveillanceTable = new HashMap<>();

        // Initialize table and assignments
        for (Enseignant enseignant : enseignants) {
            surveillanceTable.put(enseignant.getName(), new HashMap<>());
            professorAssignments.put(enseignant.getName(), 0);
        }

        // Process each exam
        for (Exam exam : exams) {
            assignTournant(surveillanceTable, professorAssignments, exam);
            assignSurveillants(surveillanceTable, professorAssignments, exam);
        }

        assignReservists(surveillanceTable, enseignants, professorAssignments);
        return surveillanceTable;
    }

    private void assignTournant(Map<String, Map<String, String>> table, Map<String, Integer> assignments, Exam exam) {
        Enseignant tournant = exam.getEnseignant();
        table.get(tournant.getName()).put(getSessionKey(exam), "TT");
        assignments.put(tournant.getName(), assignments.getOrDefault(tournant.getName(), 0) + 1);
    }

    private void assignSurveillants(Map<String, Map<String, String>> table, Map<String, Integer> assignments, Exam exam) {
        for (Local local : exam.getLocaux()) {
            int requiredSurveillants = calculateRequiredSurveillants(local.getTaille());
            assignToLocale(table, assignments, exam, local, requiredSurveillants);
        }
    }

    private void assignToLocale(Map<String, Map<String, String>> table, Map<String, Integer> assignments, Exam exam,
                                Local local, int requiredSurveillants) {
        int assignedCount = 0;

        for (String professorName : table.keySet()) {
            if (assignedCount >= requiredSurveillants) break;
            if (assignments.getOrDefault(professorName, 0) >= 1) continue; // Max 1 session per day

            table.get(professorName).put(getSessionKey(exam), local.getNom());
            assignments.put(professorName, assignments.getOrDefault(professorName, 0) + 1);
            assignedCount++;
        }
    }

    private void assignReservists(Map<String, Map<String, String>> table, List<Enseignant> enseignants,
                                  Map<String, Integer> assignments) {
        for (String session : getSessionSlots()) {
            int reservistCount = 0;

            for (Enseignant enseignant : enseignants) {
                if (reservistCount >= 10) break;
                if (assignments.getOrDefault(enseignant.getName(), 0) >= 1) continue;

                table.get(enseignant.getName()).put(session, "RR");
                assignments.put(enseignant.getName(), assignments.getOrDefault(enseignant.getName(), 0) + 1);
                reservistCount++;
            }
        }
    }

    private int calculateRequiredSurveillants(int roomCapacity) {
        if (roomCapacity >= 80) return 4;
        if (roomCapacity >= 65) return 3;
        return 2;
    }

    private String getSessionKey(Exam exam) {
        return exam.getDate() + " " + exam.getStartTime() + "-" + exam.getEndTime();
    }

    private List<String> getSessionSlots() {
        return Arrays.asList("Morning1", "Morning2", "Afternoon1", "Afternoon2");
    }
}

