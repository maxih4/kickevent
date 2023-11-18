package com.example.kickevent.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class UsernameAlreadyTakenAdvice {

    @ResponseBody
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String UsernameAlreadyTakenHandler(UsernameAlreadyTakenException ex){
        return ex.getMessage();
    }
}
