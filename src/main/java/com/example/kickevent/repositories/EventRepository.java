package com.example.kickevent.repositories;

import com.example.kickevent.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {

    public Optional<Event> findById(Long id);

}
