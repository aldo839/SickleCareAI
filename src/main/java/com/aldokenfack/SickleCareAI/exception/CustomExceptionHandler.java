package com.aldokenfack.SickleCareAI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e){

        ApiError apiError = new ApiError();

        apiError.setMessage(apiError.getMessage());
        apiError.setError(apiError.getError());
        apiError.setTimestamp(apiError.getTimestamp());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistException(UserAlreadyExistException e){

        ApiError apiError = new ApiError();

        apiError.setMessage(apiError.getMessage());
        apiError.setError(apiError.getError());
        apiError.setTimestamp(apiError.getTimestamp());

        return new ResponseEntity<>(apiError, HttpStatus.OK);
    }

}
