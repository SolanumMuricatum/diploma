package com.pepino.albumservice.entity;

import com.pepino.albumservice.entity.extra.ParticipantId;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "participant", schema = "public")
public class Participant {
    @EmbeddedId
    private ParticipantId id;
}
