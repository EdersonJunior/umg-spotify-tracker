package com.app.umg.dto;

public record TrackDTO(
        String isrc,
        String name,
        String artistName,
        String albumName,
        String albumId,
        boolean explicit,
        int playbackSeconds
) {}