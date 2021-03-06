package com.lfmelo.resources.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lfmelo.exceptions.DataIntegrityViolationException;
import com.lfmelo.exceptions.NotFoundException;
import com.lfmelo.exceptions.ValidationError;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<StandardError> objectNotFoundException(NotFoundException ex, HttpServletRequest request) {
		
		StandardError error = new StandardError();
		error.setDate(LocalDateTime.now());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage("Not Found Exception");
		error.setError(ex.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> objectNotFoundException(DataIntegrityViolationException ex, HttpServletRequest request) {
		
		StandardError error = new StandardError();
		error.setDate(LocalDateTime.now());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage("Violação de dados");
		error.setError(ex.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	} 
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validationErrosException(MethodArgumentNotValidException ex, HttpServletRequest request) {
		
		ValidationError errors = new ValidationError();
		errors.setDate(LocalDateTime.now());
		errors.setStatus(HttpStatus.BAD_REQUEST.value());
		errors.setError("Validation Error");
		errors.setMessage("Erro na validação dos campos");
		errors.setPath(request.getRequestURI());
		
		for(FieldError erro: ex.getBindingResult().getFieldErrors()) {
			errors.addError(erro.getField(), erro.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	} 
	
}
