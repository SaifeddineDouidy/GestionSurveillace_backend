package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomDeFiliere;
    private String annee;
    private int nbrInscrit;

    @OneToMany(mappedBy = "option", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Module> modules;
    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonBackReference
    private Departement departement;



}
