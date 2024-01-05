package com.example.kickevent.services;

import com.example.kickevent.model.Event;
import com.example.kickevent.model.User;
import com.example.kickevent.repositories.EventRepository;
import com.example.kickevent.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Event> getAll(String sortBy, String search) {
        List<Event> returnlist;
        if (sortBy != null && !sortBy.isBlank() && sortBy.contains(",")){
            try{
                String filterOne = sortBy.split(",")[0];
                String filterTwo = sortBy.split(",")[1];
                Sort sort = Sort.by(filterOne);
                if(filterTwo.equals("desc")){
                    sort = sort.descending();
                }

                returnlist = this.eventRepository.findAll(sort);
                if (search!= null && !search.isBlank()){
                    return returnlist.stream().filter(event->filter(event,search)).toList();
                }
                return returnlist;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (search!= null && !search.isBlank()){

            return this.eventRepository.findAll().stream().filter(event->filter(event,search)).toList();
        }
        return this.eventRepository.findAll();

    }

    public boolean filter(Event event, String search){
        return event.toString().toLowerCase().contains(search);
    }

    public Event createEvent(Event event, Authentication auth) {
        Event newEvent = Event.builder()
                .title(event.getTitle())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .streetName(event.getStreetName())
                .postalCode(event.getPostalCode())
                .houseNumber(event.getHouseNumber())
                .city(event.getCity())
                .content(event.getContent())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .owner(userRepository.findByUserName(auth.getName()).orElseThrow(() -> new RuntimeException("Wrong Owner")))
                .createdDate(Date.from(Instant.now()))
                .build();
        LOGGER.info(newEvent.toString());
        return this.eventRepository.save(newEvent);
    }


    public Event save(Event event) {

        return this.eventRepository.save(event);
    }


    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public void delete(Event event) {
        eventRepository.delete(event);
    }
}
