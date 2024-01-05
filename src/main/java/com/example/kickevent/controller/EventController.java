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
    List<Event> all(@RequestParam(required = false) String sort, @RequestParam(required = false) String search) {
        return eventService.getAll(sort,search);
    }

    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @PostMapping("/api/event")
    Event create(@RequestBody Event newEvent, Authentication auth) {
        if (newEvent.getContent() == null || newEvent.getContent().isEmpty() ||
                newEvent.getCity() == null || newEvent.getCity().isEmpty() || newEvent.getEndDate() == null || newEvent.getStartDate() == null || newEvent.getStreetName() == null
                || newEvent.getStreetName().isEmpty() || newEvent.getHouseNumber() == null || newEvent.getHouseNumber().isEmpty() || newEvent.getPostalCode() == null ||newEvent.getLongitude() ==null || newEvent.getLatitude()==null) {
            throw new RuntimeException("One or more fields are empty");

        } else {
            return eventService.createEvent(newEvent, auth);
        }

    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/api/event/{id}")
    EntityModel<Event> one(@PathVariable Long id) {
        Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        return EntityModel.of(event, linkTo(WebMvcLinkBuilder.methodOn(EventController.class).one(id)).withSelfRel(),
                linkTo(WebMvcLinkBuilder.methodOn(EventController.class).all(null,null)).withRel("Events"));
    }


    //TODO: New Exception that the Event hasnt been found
    @PreAuthorize("hasAuthority('ADMIN') || @eventService.findById(#id).get().owner.userName==authentication.name")
    @PutMapping("/api/event/{id}")
    Event edit(@RequestBody Event newEvent, @PathVariable("id") Long id) {
        return eventService.findById(id).map(Event -> {
            if (newEvent.getStartDate() != null) Event.setStartDate(newEvent.getStartDate());
            if (newEvent.getEndDate() != null) Event.setEndDate(newEvent.getEndDate());
            if (newEvent.getCity() != null) Event.setCity(newEvent.getCity());
            if (newEvent.getHouseNumber() != null) Event.setHouseNumber(newEvent.getHouseNumber());
            if (newEvent.getStreetName() != null) Event.setStreetName(newEvent.getStreetName());
            if (newEvent.getTitle() != null) Event.setTitle(newEvent.getTitle());
            if (newEvent.getContent() != null) Event.setContent(newEvent.getContent());
            if (newEvent.getPostalCode() != null) Event.setPostalCode(newEvent.getPostalCode());
            if (newEvent.getLatitude() != null) Event.setLatitude(newEvent.getLatitude());
            if (newEvent.getLongitude() != null) Event.setLongitude(newEvent.getLongitude());
            return eventService.save(Event);
        }).orElseThrow(() -> new RuntimeException("Event with that ID not found"));
    }

    //TODO: New Exception that the Event hasnt been found
    @PreAuthorize("hasAuthority('ADMIN') || @eventService.findById(#id).get().owner.userName==authentication.name")
    @DeleteMapping("/api/event/{id}")
    ResponseEntity<?> delete(@PathVariable("id") Long id) {
        eventService.delete(eventService.findById(id).orElseThrow(() -> new RuntimeException("Event with that ID not found")));
        return new ResponseEntity<>("Event with ID: " + id + " deleted", HttpStatus.ACCEPTED);
    }
}
