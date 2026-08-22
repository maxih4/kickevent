package com.example.kickevent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class JwtTokenExpiredAdvice {

    @ResponseBody
    @ExceptionHandler(JwtTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<?> JwtTokenExpiredHandler(JwtTokenExpiredException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.UNAUTHORIZED);
    }
}
