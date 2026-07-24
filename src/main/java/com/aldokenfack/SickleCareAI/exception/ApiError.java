package com.aldokenfack.SickleCareAI.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ApiError {

    private String message;

    private int error;

    private LocalDateTime timestamp;

}
