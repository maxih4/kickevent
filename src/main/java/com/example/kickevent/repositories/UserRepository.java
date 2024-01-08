package com.example.kickevent.repositories;

import com.example.kickevent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
/*
public interface PersonRepository extends CrudRepository<Person, Long> {
    List<Person> findByLastName(String lastName);
    Iterable<Person> findAll();
}
*/

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    List<User> findAll();


}