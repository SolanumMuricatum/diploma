package com.pepino.backend.service.impl;

import com.pepino.backend.dto.EventDTO;
import com.pepino.backend.entity.Event;
import com.pepino.backend.exception.EventException;
import com.pepino.backend.repository.EventRepository;
import com.pepino.backend.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public List<Event> getEvents(UUID userUUID) {
        return List.of();
    }

    @Override
    public EventDTO getEvent(UUID eventUUID) throws EventException {
        Optional<EventDTO> eventDTOOptional = Optional.ofNullable(eventRepository.getEventDTO(eventUUID));
        if(eventDTOOptional.isPresent()) {
            return eventDTOOptional.get();
        }
        else {
            throw new EventException("События с таким идентификатором не существует");
        }
    }
}