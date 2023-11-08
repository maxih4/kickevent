package com.example.kickevent.repositories;

import com.example.kickevent.data.Person;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
/*
public interface PersonRepository extends CrudRepository<Person, Long> {
    List<Person> findByLastName(String lastName);
    Iterable<Person> findAll();
}
*/

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByLastName(String lastName);
    List<Person> findAll();

    Person findOneByFirstNameAndLastName(String firstName, String lastName);
}