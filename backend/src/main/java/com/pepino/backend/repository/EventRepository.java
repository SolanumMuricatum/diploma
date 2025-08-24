package com.pepino.backend.repository;

import com.pepino.backend.dto.EventDTO;
import com.pepino.backend.entity.Event;
import com.pepino.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("SELECT new com.pepino.backend.dto.EventDTO" +
            "(e.name, e.startDate, e.endDate, u.name, u.surname) " +
            "FROM Event e " +
            "LEFT JOIN User u ON e.creator.id = u.id " +
            "WHERE e.id = :eventId ")
    EventDTO getEventDTO(@Param("eventId") UUID eventId);
}
