package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Setter
    @Column(name = "isValid", nullable = false)
    private boolean isValid = false;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "morning_start1", nullable = false)

    private String morningStart1;

    @Column(name = "morning_end1", nullable = false)

    private String morningEnd1;

    @Column(name = "morning_start2", nullable = false)

    private String morningStart2;

    @Column(name = "morning_end2", nullable = false)

    private String morningEnd2;

    @Column(name = "afternoon_start1", nullable = false)

    private String afternoonStart1;

    @Column(name = "afternoon_end1", nullable = false)

    private String afternoonEnd1;

    @Column(name = "afternoon_start2", nullable = false)

    private String afternoonStart2;

    @Column(name = "afternoon_end2", nullable = false)

    private String afternoonEnd2;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Exam> exams;

    @PreRemove
    private void preRemove() {
        for (Exam exam : exams) {
            exam.setSession(null);
        }
        exams.clear();
    }
}

