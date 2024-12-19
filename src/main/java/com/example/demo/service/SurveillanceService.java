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
    private EnseignantRepository enseignantRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public Map<String, Map<String, String>> generateSurveillanceTable(Long departmentId, List<Exam> exams) {
        // Get eligible professors
        List<Enseignant> professors = enseignantRepository.findByDepartmentIdAndDispenseFalse(departmentId);
        Map<String, Integer> professorAssignments = new HashMap<>();

        // Initialize the surveillance table
        Map<String, Map<String, String>> surveillanceTable = new HashMap<>();
        for (Enseignant professor : professors) {
            surveillanceTable.put(professor.getName(), new HashMap<>());
            professorAssignments.put(professor.getName(), 0);
        }

        // Process each exam
        for (Exam exam : exams) {
            assignTournant(surveillanceTable, professorAssignments, exam);
            assignSurveillants(surveillanceTable, professors, professorAssignments, exam);
        }

        assignReservists(surveillanceTable, professors, professorAssignments);

        return surveillanceTable;
    }

    private void assignTournant(Map<String, Map<String, String>> table, Map<String, Integer> assignments, Exam exam) {
        String tournant = exam.getEnseignant().getName();
        table.get(tournant).put(getSessionKey(exam), "TT");
        assignments.put(tournant, assignments.getOrDefault(tournant, 0) + 1);
    }

    private void assignSurveillants(Map<String, Map<String, String>> table, List<Enseignant> professors,
                                    Map<String, Integer> assignments, Exam exam) {
        int requiredSurveillants = calculateRequiredSurveillants(exam.getLocaux().stream().mapToInt(Local::getTaille).sum());
        int assignedCount = 0;

        for (Enseignant professor : professors) {
            if (assignedCount >= requiredSurveillants) break;
            String professorName = professor.getName();

            if (table.get(professorName).containsKey(getSessionKey(exam))) continue;
            if (assignments.getOrDefault(professorName, 0) >= 2) continue;

            table.get(professorName).put(getSessionKey(exam), exam.getLocaux().get(0).getNom());
            assignments.put(professorName, assignments.getOrDefault(professorName, 0) + 1);
            assignedCount++;
        }
    }

    private void assignReservists(Map<String, Map<String, String>> table, List<Enseignant> professors,
                                  Map<String, Integer> assignments) {
        for (String session : getSessionSlots()) {
            int reservistCount = 0;

            for (Enseignant professor : professors) {
                if (reservistCount >= 10) break;
                String professorName = professor.getName();

                if (table.get(professorName).containsKey(session)) continue;
                if (assignments.getOrDefault(professorName, 0) >= 2) continue;

                table.get(professorName).put(session, "RR");
                assignments.put(professorName, assignments.getOrDefault(professorName, 0) + 1);
                reservistCount++;
            }
        }
    }

    private int calculateRequiredSurveillants(int totalCapacity) {
        if (totalCapacity >= 80) return 4;
        if (totalCapacity >= 65) return 3;
        return 2;
    }

    private String getSessionKey(Exam exam) {
        return exam.getDate() + " " + exam.getStartTime() + "-" + exam.getEndTime();
    }

    private List<String> getSessionSlots() {
        return Arrays.asList("Morning1", "Morning2", "Afternoon1", "Afternoon2");
    }
}
