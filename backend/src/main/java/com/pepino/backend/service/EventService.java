package com.pepino.backend.service;

import com.pepino.backend.dto.EventDto;
import com.pepino.backend.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {
    public void saveEvent(Event event, UUID creatorId);
    public List<Event> getEvents(UUID userUUID);
    public EventDto getEvent(UUID eventUUID) throws Exception;
    public void putEvent(Event event, UUID id) throws Exception;
}
