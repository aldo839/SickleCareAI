package com.aldokenfack.SickleCareAI.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Doctor extends User{

    private String firstname;

    private String lastname;

    private String speciality;

    private String matricule;

    private String validationLetterReference;

    private String hospitalUnit;

    private String region;

    private String city;

    private String hospital;

}
