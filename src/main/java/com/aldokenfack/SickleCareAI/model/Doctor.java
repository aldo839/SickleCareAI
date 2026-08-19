package com.aldokenfack.SickleCareAI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Doctor extends User{

    private String firstname;

    private String lastname;

    private String speciality;

    private String matricule;

    private String validationLetterReference;

    private String region;

    private String city;

    private String hospital;

    private String hospitalUnit;

    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;

}

