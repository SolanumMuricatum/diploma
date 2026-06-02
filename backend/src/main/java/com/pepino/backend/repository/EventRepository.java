package com.pepino.backend.repository;

import com.pepino.backend.dto.EventDto;
import com.pepino.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("SELECT new com.pepino.backend.dto.EventDto" +
            "(e.name, e.background, e.textColor, e.textFont, e.textSize, e.startDate, e.endDate, u.login) " +
            "FROM Event e " +
            "LEFT JOIN User u ON e.creator.id = u.id " +
            "WHERE e.id = :eventId ")
    EventDto getEventDTO(@Param("eventId") UUID eventId);
}
