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
@Table(name = "album_member")
public class AlbumMember {
    @EmbeddedId
    private AlbumMemberId id;
}
