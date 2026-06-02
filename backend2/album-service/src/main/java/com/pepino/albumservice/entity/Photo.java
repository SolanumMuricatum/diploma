package com.pepino.albumservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "image")
    private String image;
    @Column(name = "album_id")
    private UUID albumId;
    @Column(name = "user_id")
    private UUID userId;
}
