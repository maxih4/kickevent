package com.example.kickevent.controller;

import com.example.kickevent.exceptions.EventNotFoundException;
import com.example.kickevent.model.Event;
import com.example.kickevent.services.EventService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        super();
        this.eventService = eventService;
    }

    @GetMapping("/api/event")
    List<Event> all() {
        return eventService.getAll();
    }

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @PostMapping("/api/event")
    Event create(@RequestBody Event newEvent, Authentication auth) {

        return eventService.createEvent(newEvent, auth);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/api/event/{id}")
    EntityModel<Event> one(@PathVariable Long id) {
        Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        return EntityModel.of(event, linkTo(WebMvcLinkBuilder.methodOn(EventController.class).one(id)).withSelfRel(),
                linkTo(WebMvcLinkBuilder.methodOn(EventController.class).all()).withRel("Events"));
    }


    //TODO: New Exception that the Event hasnt been found
    @PreAuthorize("hasAuthority('ADMIN') || @eventService.findById(#id).get().owner==authentication.name")
    @PutMapping("/api/event/{id}")
    Event edit(@RequestBody Event newEvent, @PathVariable("id") Long id) {
        return eventService.findById(id).map(Event -> {
            Event.setStartDate(newEvent.getStartDate());
            Event.setEndDate(newEvent.getEndDate());
            Event.setCity(newEvent.getCity());
            Event.setHouseNumber(newEvent.getHouseNumber());
            Event.setStreetName(newEvent.getStreetName());
            Event.setTitle(newEvent.getTitle());
            Event.setContent(newEvent.getContent());
            return eventService.save(Event);
        }).orElseThrow(() -> new RuntimeException("Event with that ID not found"));
    }

    //TODO: New Exception that the Event hasnt been found
    @PreAuthorize("hasAuthority('ADMIN') || @eventService.findById(#id).get().owner==authentication.name")
    @DeleteMapping("/api/event/{id}")
    ResponseEntity<?> delete(@PathVariable("id") Long id) {
        eventService.delete(eventService.findById(id).orElseThrow(() -> new RuntimeException("Event with that ID not found")));
        return new ResponseEntity<>("Event with ID: " + id + " deleted", HttpStatus.ACCEPTED);
    }
}
