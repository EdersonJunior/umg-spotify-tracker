package com.app.umg.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackEntity {

    @Id
    private String isrc;

    private String name;
    private String artistName;
    private String albumName;
    private String albumId;
    private boolean explicit;
    private int playbackSeconds;

    private String coverPath;
}
