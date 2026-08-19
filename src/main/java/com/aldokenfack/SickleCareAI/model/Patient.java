package com.aldokenfack.SickleCareAI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Patient extends User{

    private String firstname;

    private String lastname;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Genotype genotype;

    @Column(name = "blood_type")
    @Enumerated(EnumType.STRING)
    private Bloodtype bloodtype;

    private Double weight;

    private String region;

    private String city;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

}
