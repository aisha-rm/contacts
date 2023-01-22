package com.ltp.contacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ltp.contacts.exception.ContactNotFoundException;
import com.ltp.contacts.exception.ErrorResponse;


@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<Object> handleContactNotFoundException(ContactNotFoundException ex) {
      ErrorResponse error = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()));  
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    //to handlle errors from field validation, not using binding result in the post/put mapping
    //override inherited method as below
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> allErrors = new ArrayList<>();
        for (ObjectError error :  ex.getBindingResult().getAllErrors()){  //returns list of errors from validation process now being looped thru
          allErrors.add(error.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse(allErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
