package com.example.demo.service;

import com.example.demo.model.Enseignant;
import com.example.demo.model.Exam;
import com.example.demo.model.Local;
import com.example.demo.model.Session;
import com.example.demo.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SurveillanceService {

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private ExamService examService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private OccupationService occupationService; // Added to fetch occupations

    public Map<Long, Map<String, String>> generateSurveillanceTable(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        List<Exam> exams = examService.findBySessionId(sessionId);
        List<Enseignant> enseignants = enseignantService.getAllEnseignants();
        Map<Long, Integer> professorAssignments = new HashMap<>();
        Map<Long, Map<String, String>> surveillanceTable = new HashMap<>();

        // Initialize table and assignments
        for (Enseignant enseignant : enseignants) {
            surveillanceTable.put(enseignant.getId(), new HashMap<>());
            professorAssignments.put(enseignant.getId(), 0);
        }

        // Process each exam
        for (Exam exam : exams) {
            assignTournant(surveillanceTable, professorAssignments, exam);
            assignSurveillants(surveillanceTable, professorAssignments, exam);
        }

        // Assign reservists based on session
        assignReservists(surveillanceTable, enseignants, professorAssignments, session);

        return surveillanceTable;
    }

    private void assignTournant(Map<Long, Map<String, String>> table, Map<Long, Integer> assignments, Exam exam) {
        Enseignant tournant = exam.getEnseignant();

        if (!hasConflict(tournant, exam.getDate(), exam.getStartTime(), exam.getEndTime())) {
            table.get(tournant.getId()).put(getSessionKey(exam), "TT");
            assignments.put(tournant.getId(), assignments.getOrDefault(tournant.getId(), 0) + 1);
        }
    }

    private void assignSurveillants(Map<Long, Map<String, String>> table, Map<Long, Integer> assignments, Exam exam) {
        for (Local local : exam.getLocaux()) {
            int requiredSurveillants = calculateRequiredSurveillants(local.getTaille());
            assignToLocale(table, assignments, exam, local, requiredSurveillants);
        }
    }

    private void assignToLocale(Map<Long, Map<String, String>> table, Map<Long, Integer> assignments, Exam exam,
                                Local local, int requiredSurveillants) {
        int assignedCount = 0;

        for (Enseignant enseignant : enseignantService.getAllEnseignants()) {
            if (assignedCount >= requiredSurveillants) break;
            if (assignments.getOrDefault(enseignant.getId(), 0) >= 1) continue; // Max 1 session per day

            if (!hasConflict(enseignant, exam.getDate(), exam.getStartTime(), exam.getEndTime())) {
                table.get(enseignant.getId()).put(getSessionKey(exam), local.getNom());
                assignments.put(enseignant.getId(), assignments.getOrDefault(enseignant.getId(), 0) + 1);
                assignedCount++;
            }
        }
    }

    private void assignReservists(Map<Long, Map<String, String>> table, List<Enseignant> enseignants,
                                  Map<Long, Integer> assignments, Session session) {
        LocalDate currentDate = session.getStartDate();

        // Iterate over each day in the session
        while (!currentDate.isAfter(session.getEndDate())) {
            // Morning session (from morning_start1 to morning_end2)
            assignReservistsForHalfDay(
                    table, enseignants, assignments, currentDate,
                    session.getMorningStart1() + "-" + session.getMorningEnd2(), "Morning"
            );

            // Afternoon session (from afternoon_start1 to afternoon_end2)
            assignReservistsForHalfDay(
                    table, enseignants, assignments, currentDate,
                    session.getAfternoonStart1() + "-" + session.getAfternoonEnd2(), "Afternoon"
            );

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }
    }

    private void assignReservistsForHalfDay(Map<Long, Map<String, String>> table, List<Enseignant> enseignants,
                                            Map<Long, Integer> assignments, LocalDate date, String timeRange, String halfDay) {
        int reservistCount = 0;

        for (Enseignant enseignant : enseignants) {
            if (reservistCount >= 10) break;
            if (enseignant.isDispense()) continue;
            if (assignments.getOrDefault(enseignant.getId(), 0) >= 1) continue; // Skip if already assigned to a session

            String[] times = timeRange.split("-");
            LocalTime startTime = LocalTime.parse(times[0]);
            LocalTime endTime = LocalTime.parse(times[1]);

            if (!hasConflict(enseignant, date, startTime, endTime)) {
                String sessionKey = date + " " + halfDay + " (" + timeRange + ")"; // E.g., "2024-12-21 Morning (08:00-12:30)"
                table.get(enseignant.getId()).put(sessionKey, "RR");
                assignments.put(enseignant.getId(), assignments.getOrDefault(enseignant.getId(), 0) + 1);
                reservistCount++;
            }
        }
    }

    private boolean hasConflict(Enseignant enseignant, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return occupationService.getOccupationsByEnseignantAndDate(enseignant.getId(), date).stream()
                .anyMatch(occupation ->
                        !(occupation.getEndTime().isBefore(startTime) || occupation.getStartTime().isAfter(endTime))
                );
    }

    private int calculateRequiredSurveillants(int roomCapacity) {
        if (roomCapacity >= 80) return 4;
        if (roomCapacity >= 65) return 3;
        return 2;
    }

    private String getSessionKey(Exam exam) {
        return exam.getDate() + " " + exam.getStartTime() + "-" + exam.getEndTime();
    }
}
