package com.pepino.albumservice.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "album", schema = "public")
public class Album {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "background")
    private String background;
    @Column(name = "text_color")
    private String textColor;
    @Column(name = "text_font")
    private String textFont;
    @Column(name = "text_size")
    private String textSize;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "creator_id")
    private UUID creatorId;
    @Column(name = "creator_login_snapshot")
    private String creatorLoginSnapshot;
}

