package com.pepino.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDTO {
    private String name;
    private String background;
    private String textColor;
    private String textFont;
    private String textSize;
    private LocalDate startDate;
    private LocalDate endDate;
    private String creatorName;
    private String creatorSurname;
}
