package com.example.kickevent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class JwtTokenExpiredAdvice {

    @ResponseBody
    @ExceptionHandler(JwtTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String JwtTokenExpiredHandler(JwtTokenExpiredException ex){
        return ex.getMessage();
    }
}
