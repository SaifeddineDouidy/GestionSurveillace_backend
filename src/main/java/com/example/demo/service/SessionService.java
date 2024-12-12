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
        sessionRepository.save(existingSession);
    }
}
