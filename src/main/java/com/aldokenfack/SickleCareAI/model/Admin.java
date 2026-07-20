package com.aldokenfack.SickleCareAI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "administrator")
@Getter @Setter
public class Admin {

    private String firstname;
    private String lastname;

}
