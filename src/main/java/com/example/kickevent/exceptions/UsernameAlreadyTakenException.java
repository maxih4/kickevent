package com.example.kickevent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UsernameAlreadyTakenException extends RuntimeException{

    public UsernameAlreadyTakenException(String userName){
        super("Username " + userName + " already taken");

    }
}
