package com.pepino.backend.service.impl;

import com.pepino.backend.dto.EventDto;
import com.pepino.backend.entity.Event;
import com.pepino.backend.entity.User;
import com.pepino.backend.exception.EventException;
import com.pepino.backend.repository.EventRepository;
import com.pepino.backend.repository.UserRepository;
import com.pepino.backend.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void saveEvent(Event event, UUID creatorId) {

        event.setStartDate(LocalDate.now());
        event.setEndDate(LocalDate.now().plusMonths(1));
        event.setCreator(new User(creatorId)); //проверить пользователя, что он есть
        eventRepository.save(event);
    }

    @Override
    public List<Event> getEvents(UUID userUUID) {
        return List.of();
    }

    @Override
    public EventDto getEvent(UUID eventUUID) throws EventException {
        Optional<EventDto> eventDTOOptional = Optional.ofNullable(eventRepository.getEventDTO(eventUUID));
        if(eventDTOOptional.isPresent()) {
            return eventDTOOptional.get();
        }
        else {
            throw new EventException("События с таким идентификатором не существует");
        }
    }

    @Override
    public void putEvent(Event event, UUID id) throws EventException {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(eventOptional.isPresent()) {
            Event updatedEvent = eventOptional.get();
            updatedEvent.setName(event.getName());
            updatedEvent.setBackground(event.getBackground());
            updatedEvent.setTextColor(event.getTextColor());
            updatedEvent.setTextSize(event.getTextSize());
            updatedEvent.setTextFont(event.getTextFont());
            eventRepository.save(updatedEvent);
        }
        else {
            throw new EventException("События с таким идентификатором не существует");
        }
    }

}