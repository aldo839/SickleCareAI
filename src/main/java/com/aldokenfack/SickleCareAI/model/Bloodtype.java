package com.aldokenfack.SickleCareAI.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Bloodtype {

    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String value;

    Bloodtype(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }

    @JsonCreator
    public static Bloodtype fromValue(String value){

        return Arrays.stream(Bloodtype.values())
                .filter(b -> b.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Blood Type: " + value + ". Must be one of: A+, A-, B+, B-, AB+, AB-, O+, O-"));
    }
}
