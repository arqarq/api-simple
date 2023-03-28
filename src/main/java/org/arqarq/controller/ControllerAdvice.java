package org.arqarq.controller;

import jakarta.validation.ConstraintViolationException;
import org.arqarq.openapi.model.ValidationMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationMessages handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ValidationMessages().validationMessages(ex.getAllErrors().stream().map(it -> {
            String defaultMessage = it.getDefaultMessage();
            return it.toString().split(";", 2)[0] + (defaultMessage != null ? ": " + defaultMessage.replace('"', '\'') : "");
        }).collect(Collectors.toList()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationMessages handleConstraintViolationException(ConstraintViolationException ex) {
        return new ValidationMessages().validationMessages(List.of(ex.getMessage().replace('"', '\'')));
    }
}