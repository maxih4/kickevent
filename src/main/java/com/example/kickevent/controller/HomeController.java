package com.example.kickevent.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HomeController {

@GetMapping
    public String home(Principal principal){
    return "Hello World!" + principal.getName();
}


}
