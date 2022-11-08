package com.softserve.itacademy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullEntityReferenceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void nullEntityReferenceExceptionHandler(HttpServletRequest request, NullEntityReferenceException exception) {
        logException(request, exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value= HttpStatus.FORBIDDEN)
    public void accessDeniedExceptionHandler(HttpServletRequest request, Exception exception) {
        logException(request, exception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public void entityNotFoundExceptionHandler(HttpServletRequest request, EntityNotFoundException exception) {
        logException(request, exception);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value= HttpStatus.CONFLICT)
    public void duplicateEntityExceptionHandler(HttpServletRequest request, DuplicateEntityException exception) {
        logException(request, exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public void internalServerErrorHandler(HttpServletRequest request, Exception exception) {
        logException(request, exception);
    }

    private void logException(HttpServletRequest request, Exception exception) {
        logger.error("Exception raised = {} :: URL = {}", exception.getMessage(), request.getRequestURL());
    }
}
