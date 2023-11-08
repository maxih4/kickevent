package com.example.kickevent.configuration;

import com.example.kickevent.repositories.PersonRepository;
import com.example.kickevent.services.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class PersonConfiguration {

    @Bean
    public PersonService personService(PersonRepository personRepository){
        return new PersonService(personRepository);
    }
}
