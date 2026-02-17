package com.example.smart_stylist.controller;

import com.example.smart_stylist.dto.EventRequest;  // ← ДОБАВИЛ
import com.example.smart_stylist.entity.Event;
import com.example.smart_stylist.service.EventService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventRequest request) {
        Event created = eventService.createEvent(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Event>> getEventsByUser(@PathVariable Long userId) {
        List<Event> events = eventService.getEventsByUserId(userId);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {  // ← ИЗМЕНИЛ
        Event updated = eventService.updateEvent(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}