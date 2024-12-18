package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Enseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "dispense")
    private boolean dispense;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonBackReference
    private Departement department;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDispense() {
        return dispense;
    }

    public void setDispense(boolean dispense) {
        this.dispense = dispense;
    }

    public Departement getDepartment() {
        return department;
    }

    public void setDepartment(Departement department) {
        this.department = department;
    }
}
