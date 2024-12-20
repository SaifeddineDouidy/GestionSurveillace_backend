package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ExamDTO {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long departement;
    private Long enseignant;
    private Long option;
    private Long module;
    private List<Long> locauxIds; // IDs of locaux to associate
    private Long sessionId; // ID of the session


}
