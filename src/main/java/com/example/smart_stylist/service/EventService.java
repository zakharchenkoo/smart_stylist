package com.example.smart_stylist.service;

import com.example.smart_stylist.dto.EventRequest;
import com.example.smart_stylist.entity.Event;
import com.example.smart_stylist.entity.Outfit;
import com.example.smart_stylist.entity.User;
import com.example.smart_stylist.exception.ResourceNotFoundException;
import com.example.smart_stylist.repository.EventRepository;
import com.example.smart_stylist.repository.OutfitRepository;
import com.example.smart_stylist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final OutfitRepository outfitRepository;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        OutfitRepository outfitRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.outfitRepository = outfitRepository;
    }

    public Event createEvent(EventRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Event event = new Event();
        event.setName(request.getName());
        event.setEventType(request.getEventType());
        event.setEventDate(request.getEventDate());
        event.setDescription(request.getDescription());
        event.setUser(user);

        if (request.getOutfitId() != null) {
            Outfit outfit = outfitRepository.findById(request.getOutfitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Outfit not found"));
            event.setOutfit(outfit);
        }

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    public List<Event> getEventsByUserId(Long userId) {
        return eventRepository.findByUserId(userId);
    }

    public Event updateEvent(Long id, EventRequest request) {
        Event event = getEventById(id);

        event.setName(request.getName());
        event.setEventType(request.getEventType());
        event.setEventDate(request.getEventDate());
        event.setDescription(request.getDescription());

        if (request.getOutfitId() != null) {
            Outfit outfit = outfitRepository.findById(request.getOutfitId())
                    .orElseThrow(() -> new ResourceNotFoundException("Outfit not found"));
            event.setOutfit(outfit);
        } else {
            event.setOutfit(null);
        }

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id " + id);
        }
        eventRepository.deleteById(id);
    }
}