package com.example.demo.service;

import com.example.demo.model.Session;
import com.example.demo.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session save(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public void delete(Long session_id) {
        sessionRepository.deleteById(session_id);
    }

    public Session find(Long session_id) {
        return sessionRepository.findById(session_id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
    }

    public void update(Session session) {
        Session existingSession = find(session.getId());
        existingSession.setType(session.getType());
        existingSession.setStartDate(session.getStartDate());
        existingSession.setEndDate(session.getEndDate());
        existingSession.setMorningStart1(session.getMorningStart1());
        existingSession.setMorningStart2(session.getMorningStart2());
        existingSession.setMorningEnd1(session.getMorningEnd1());
        existingSession.setMorningEnd2(session.getMorningEnd2());
        existingSession.setAfternoonStart1(session.getAfternoonStart1());
        existingSession.setAfternoonStart2(session.getAfternoonStart2());
        existingSession.setAfternoonEnd1(session.getAfternoonEnd1());
        existingSession.setAfternoonEnd2(session.getAfternoonEnd2());
        sessionRepository.save(existingSession);
    }
}
