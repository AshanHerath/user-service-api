package com.techheaven.userserviceapi.advisers;

import com.techheaven.userserviceapi.exception.EntryAlreadyExistsException;
import com.techheaven.userserviceapi.exception.EntryNotFoundException;
import com.techheaven.userserviceapi.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWiderExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponse> handleEntryNotFoundException(EntryNotFoundException e){
        return new ResponseEntity<>(
                new StandardResponse(404,e.getMessage(),e),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EntryAlreadyExistsException.class)
    public ResponseEntity<StandardResponse> handleEntryAlreadyExistsException(EntryAlreadyExistsException e){
        return new ResponseEntity<>(
                new StandardResponse(409, e.getMessage(), e),
                HttpStatus.CONFLICT
        );
    }
}