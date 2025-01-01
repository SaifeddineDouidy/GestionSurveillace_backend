package com.example.demo.service;

import com.example.demo.model.Occupation;
import com.example.demo.repository.OccupationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OccupationService {

    @Autowired
    private OccupationRepository occupationRepository;

    public List<Occupation> getOccupationsByEnseignantAndSession(Long enseignantId, Long sessionId) {
        return occupationRepository.findByEnseignantIdAndSessionId(enseignantId, sessionId);
    }

    public List<Occupation> getOccupationsBySessionAndDate(Long sessionId, LocalDate date) {
        return occupationRepository.findBySessionIdAndDate(sessionId, date);
    }

    public Occupation saveOccupation(Occupation occupation) {
        return occupationRepository.save(occupation);
    }

    public void deleteOccupation(Long id) {
        occupationRepository.deleteById(id);
    }
    public List<Occupation> getOccupationsByEnseignantAndDate(Long enseignantId, LocalDate date) {
        return occupationRepository.findByEnseignantIdAndDate(enseignantId, date);
    }

}
