package com.example.demo.model;

import com.example.demo.repository.LocalRepository;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "locaux")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int taille;
    private String type;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonBackReference
    private Exam exam;

    private boolean disponible = true;

    public Local() {

    }

    public Local(String name, int size, String type) {
        this.nom = name;
        this.taille = size;
        this.type = type;
    }

}
