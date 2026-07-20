package com.aldokenfack.SickleCareAI.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Patient extends User{

    private String firstname;

    private String lastname;

    private Sex sex;

    private LocalDate birthDate;
    @Column(name = "blood_type")

    private String bloodType;

    private String genotype;

    private Double weight;

    private String region;

    private String city;

}
