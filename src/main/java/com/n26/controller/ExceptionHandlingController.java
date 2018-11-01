package com.n26.controller;

import com.n26.exception.StaleTransactionException;
import com.n26.exception.InvalidJsonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */
@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(InvalidJsonException.class)
    public ResponseEntity invalidJson(InvalidJsonException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); //422
    }

    @ExceptionHandler(StaleTransactionException.class)
    public ResponseEntity illegalArgument3(StaleTransactionException ex) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
    }


}
