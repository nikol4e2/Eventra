package com.nikola.eventra.controllers;

import com.nikola.eventra.domain.dtos.ErrorDto;
import com.nikola.eventra.exceptions.EventNotFoundException;
import com.nikola.eventra.exceptions.EventUpdateException;
import com.nikola.eventra.exceptions.TicketTypeNotFoundException;
import com.nikola.eventra.exceptions.UserNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleEventUpdateException(TicketTypeNotFoundException e)
    {
        log.error("Exception occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto("Unable to update event");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException e)
    {
        log.error("Exception occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto("Event not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException e)
    {
        log.error("Exception occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto("TicketType not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e)
    {
        log.error("Exception occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto("User not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto();
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String errorMessage=fieldErrors.stream().findFirst().map(f->f.getField()+": "+f.getDefaultMessage()).orElse("Validation error occurred");
         errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException e)
    {
        log.error("Exception occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto();

        String errorMessage=e.getConstraintViolations().stream().findFirst().map(violation->violation.getPropertyPath()+": "+violation.getMessage()).orElse("Constraint violation occurred");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("Exception occurred: {}", e.getMessage());
        ErrorDto errorDto = new ErrorDto("An unknown error occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

