package com.example.demo.controller;

import com.example.demo.model.Holiday;
import com.example.demo.model.Session;
import com.example.demo.service.HolidayService;
import com.example.demo.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private HolidayService holidayService;

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        Session savedSession = sessionService.save(session);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.find(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        session.setId(id); // Ensure the ID matches
        sessionService.update(session);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Updated backend code to accept a JSON object
    @PutMapping("/{id}/validate")
    public ResponseEntity<Session> toggleValidation(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean isValid = body.get("valid");  // Ensure this matches the key in the request body
        if (isValid == null) {
            return ResponseEntity.badRequest().body(null); // Return a bad request if the key is missing
        }
        Session session = sessionService.find(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        session.setValid(isValid);
        sessionService.update(session);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<Map<String, Object>> getSessionSchedule(@PathVariable Long id) {
        Session session = sessionService.find(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> schedule = new HashMap<>();
        schedule.put("startDate", session.getStartDate());
        schedule.put("endDate", session.getEndDate());

        List<Map<String, String>> timeSlots = new ArrayList<>();

        Map<String, String> e = new HashMap<>();
        e.put("time", session.getMorningStart1() + " - " + session.getMorningEnd1());
        e.put("slot", "morning1");
        timeSlots.add(e);
        Map<String, String> e1 = new HashMap<>();
        e1.put("time", session.getMorningStart2() + " - " + session.getMorningEnd2());
        e1.put("slot", "morning2");
        timeSlots.add(e1);
        Map<String, String> e2 = new HashMap<>();
        e2.put("time", session.getAfternoonStart1() + " - " + session.getAfternoonEnd1());
        e2.put("slot", "afternoon1");
        timeSlots.add(e2);
        Map<String, String> e3 = new HashMap<>();
        e3.put("time", session.getAfternoonStart2() + " - " + session.getAfternoonEnd2());
        e3.put("slot", "afternoon2");
        timeSlots.add(e3);


        schedule.put("timeSlots", timeSlots);

        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        List<Holiday> holidays = holidayService.getAllHolidays();
        return ResponseEntity.ok(holidays);
    }

    @GetMapping("/holidays/range")
    public ResponseEntity<List<Holiday>> getHolidaysInRange(
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<Holiday> holidays = holidayService.getHolidaysInRange(start, end);
        return ResponseEntity.ok(holidays);
    }


}
