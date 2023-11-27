package com.example.kickevent.controller;

import com.example.kickevent.model.User;
import com.example.kickevent.exceptions.UserNotFoundException;
import com.example.kickevent.services.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    List<User> all() {
        return userService.getAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        return userService.createPerson(newUser.getUserName(), newUser.getPassword());
    }

    /*@GetMapping("/persons/{id}")
    Person one(@PathVariable Long id) throws Exception {
        return personService.findById(id).orElseThrow(() -> new Exception("Couldnt find Person with id" + id));

    }*/

    @PreAuthorize("hasAuthority('ADMIN') || @userService.findById(#id).get().userName==authentication.name")
    @GetMapping("/user/{id}")
    EntityModel<User> one(@PathVariable("id") Long id){
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return EntityModel.of(user, linkTo(WebMvcLinkBuilder.methodOn(UserController.class).one(id)).withSelfRel(), linkTo(WebMvcLinkBuilder.methodOn(UserController.class).all()).withRel("Users"));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/user/{id}")
    User deleteUser(@PathVariable Long id){
    return userService.delete(userService.findById(id).orElseThrow(() ->new UserNotFoundException(id)));
    }


}
