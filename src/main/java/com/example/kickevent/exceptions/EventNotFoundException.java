package com.example.kickevent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class EventNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EventNotFoundException(Long id){
        super("Could not find Event " + id);
    }
}
