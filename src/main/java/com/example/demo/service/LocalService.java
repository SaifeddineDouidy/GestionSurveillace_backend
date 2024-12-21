package com.example.demo.service;

import com.example.demo.model.Local;
import com.example.demo.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;


    public List<Local> getLocauxByIds(List<Long> ids) {
        return localRepository.findAllByIdIn(ids);
    }

    public List<Local> getAllLocaux() {
        return localRepository.findAll();
    }

    public Local addLocal(Local local) {
        return localRepository.save(local);
    }

    public void deleteLocal(Local local) {
        localRepository.delete(local);
    }

    public void deleteLocalById(Long id) {
        localRepository.deleteById(id);
    }

    public Local updateLocal(Local local) {
        return localRepository.save(local);
    }

    public List<Local> getAvailableLocaux() {
        return localRepository.findByDisponibleTrue();
    }

    public List<Local> searchLocaux(String query) {
        return localRepository.findAll().stream()
                .filter(local -> local.getNom().toLowerCase().contains(query.toLowerCase())
                        || String.valueOf(local.getTaille()).contains(query)
                        || local.getType().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
    public void saveAll(List<Local> locaux) {
        localRepository.saveAll(locaux);
    }

}
