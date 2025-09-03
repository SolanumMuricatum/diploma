package com.pepino.backend.service;

import com.pepino.backend.dto.EventDTO;
import com.pepino.backend.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {
    public void saveEvent(Event event, UUID creatorId);
    public List<Event> getEvents(UUID userUUID);
    public EventDTO getEvent(UUID eventUUID) throws Exception; ;
}
