package com.pepino.backend.controller;

import com.pepino.backend.entity.Event;
import com.pepino.backend.entity.User;
import com.pepino.backend.service.EventService;
import com.pepino.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveEvent(@RequestBody Event event, @RequestParam UUID creatorId) throws Exception {
        eventService.saveEvent(event, creatorId);
        return ResponseEntity.status(201).body(event);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getEvent(@RequestParam String id) throws Exception {
        return ResponseEntity.status(200).body(eventService.getEvent(UUID.fromString(id)));
    }
}
