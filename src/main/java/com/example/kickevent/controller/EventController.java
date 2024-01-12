package com.example.kickevent.controller;

import com.example.kickevent.exceptions.EventNotFoundException;
import com.example.kickevent.model.Event;
import com.example.kickevent.services.EventService;
import org.springframework.data.domain.Page;
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
    Page<Event> all(@RequestParam(required = false) String sort, @RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        return eventService.getAll(sort,search,size,page);
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
                linkTo(WebMvcLinkBuilder.methodOn(EventController.class).all(null,null,0,3)).withRel("Events"));
    }


    //TODO: New Exception that the Event hasnt been found
    @PreAuthorize("hasAuthority('ADMIN') || @eventService.findById(#id).get().owner.userName==authentication.name")
    @PutMapping("/api/event/{id}")
    Event edit(@RequestBody Event newEvent, @PathVariable("id") Long id) {
        return eventService.findById(id).map(Event -> {
            if (newEvent.getStartDate() != null && !newEvent.getStartDate().toString().isEmpty()) Event.setStartDate(newEvent.getStartDate()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getEndDate() != null && !newEvent.getEndDate().toString().isEmpty()) Event.setEndDate(newEvent.getEndDate()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getCity() != null && !newEvent.getCity().isEmpty()) Event.setCity(newEvent.getCity()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getHouseNumber() != null && !newEvent.getHouseNumber().isEmpty()) Event.setHouseNumber(newEvent.getHouseNumber()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getStreetName() != null && !newEvent.getStreetName().isEmpty())  Event.setStreetName(newEvent.getStreetName()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getTitle() != null && !newEvent.getTitle().isEmpty()) Event.setTitle(newEvent.getTitle()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getContent() != null && !newEvent.getContent().isEmpty()) Event.setContent(newEvent.getContent()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getPostalCode() != null && !newEvent.getPostalCode().toString().isEmpty()) Event.setPostalCode(newEvent.getPostalCode()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getLatitude() != null && !newEvent.getLatitude().isEmpty()) Event.setLatitude(newEvent.getLatitude()); else  throw new RuntimeException("One or more fields are empty");
            if (newEvent.getLongitude() != null && !newEvent.getLongitude().isEmpty()) Event.setLongitude(newEvent.getLongitude()); else  throw new RuntimeException("One or more fields are empty");
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
