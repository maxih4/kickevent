package com.example.kickevent.controller;

import com.example.kickevent.model.User;
import com.example.kickevent.exceptions.UserNotFoundException;
import com.example.kickevent.services.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping("/user")
    List<User> all() {
        return userService.getAll();
    }

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userService.createPerson(newUser.getUserName(), newUser.getPassword());
    }

    /*@GetMapping("/persons/{id}")
    Person one(@PathVariable Long id) throws Exception {
        return personService.findById(id).orElseThrow(() -> new Exception("Couldnt find Person with id" + id));

    }*/

    @GetMapping("/user/{id}")
    EntityModel<User> one(@PathVariable Long id){
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return EntityModel.of(user, linkTo(WebMvcLinkBuilder.methodOn(UserController.class).one(id)).withSelfRel(), linkTo(WebMvcLinkBuilder.methodOn(UserController.class).all()).withRel("Users"));
    }


    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        return userService.findById(id).map(User -> {
            User.setUserName(newUser.getUserName());
            return userService.save(User);
        }).orElseGet(() -> {
            newUser.setId(id);
            return userService.save(newUser);
        });
    }

    @DeleteMapping("/user/{id}")
    User deleteUser(@PathVariable Long id){
    return userService.delete(userService.findById(id).orElseThrow(() ->new UserNotFoundException(id)));
    }


}
