package com.example.kickevent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class JwtTokenExpiredException extends ResponseStatusException{

    public JwtTokenExpiredException(String message){
        super(HttpStatus.UNAUTHORIZED, message);

    }
}
