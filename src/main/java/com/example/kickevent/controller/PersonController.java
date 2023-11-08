package com.example.kickevent.controller;

import com.example.kickevent.data.Person;
import com.example.kickevent.exceptions.PersonNotFoundException;
import com.example.kickevent.services.PersonService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        super();
        this.personService = personService;
    }

    @GetMapping("/persons")
    List<Person> all() {
        return personService.getAll();
    }

    @PostMapping("/persons")
    Person newPerson(@RequestBody Person newPerson) {
        return personService.createPerson(newPerson.getFirstName(), newPerson.getLastName());
    }

    /*@GetMapping("/persons/{id}")
    Person one(@PathVariable Long id) throws Exception {
        return personService.findById(id).orElseThrow(() -> new Exception("Couldnt find Person with id" + id));

    }*/

    @GetMapping("/persons/{id}")
    EntityModel<Person> one(@PathVariable Long id){
        Person person = personService.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
        return EntityModel.of(person, linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).one(id)).withSelfRel(), linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).all()).withRel("Persons"));
    }


    @PutMapping("/persons/{id}")
    Person updatePerson(@RequestBody Person newPerson, @PathVariable Long id) {
        return personService.findById(id).map(Person -> {
            Person.setFirstName(newPerson.getFirstName());
            Person.setLastName(newPerson.getLastName());
            return personService.save(Person);
        }).orElseGet(() -> {
            newPerson.setId(id);
            return personService.save(newPerson);
        });
    }
}
