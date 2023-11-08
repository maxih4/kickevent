package com.example.kickevent.services;

import com.example.kickevent.data.Person;
import com.example.kickevent.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository){
        super();
        this.personRepository = personRepository;
    }

    public Person createPerson(String firstName, String lastName){
        Person person = Person.builder().firstName(firstName).lastName(lastName).build();
        return personRepository.save(person);
    }

    public List<Person> getAll(){
        return personRepository.findAll();
    }

    public Person save(Person person){
        personRepository.save(person);
        return person;
    }

    public Optional<Person> findById(Long id){
        return personRepository.findById(id);
    }
}
