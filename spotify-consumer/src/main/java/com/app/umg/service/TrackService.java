package com.app.umg.service;

import com.app.umg.dto.TrackDTO;
import com.app.umg.entity.TrackEntity;
import com.app.umg.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository repository;
    private final SpotifyService spotifyService;

    public void createTrackByIsrc(String isrc) {
        if (repository.existsById(isrc)) return;

        TrackDTO dto = spotifyService.getTrackByIsrc(isrc);
        String coverPath = spotifyService.downloadAlbumCover(dto.albumId(), isrc);

        TrackEntity entity = TrackEntity.builder()
                .isrc(dto.isrc())
                .name(dto.name())
                .artistName(dto.artistName())
                .albumName(dto.albumName())
                .albumId(dto.albumId())
                .explicit(dto.explicit())
                .playbackSeconds(dto.playbackSeconds())
                .coverPath(coverPath)
                .build();

        repository.save(entity);
    }

    public TrackDTO getMetadata(String isrc) {
        return repository.findById(isrc)
                .map(t -> new TrackDTO(
                        t.getIsrc(), t.getName(), t.getArtistName(),
                        t.getAlbumName(), t.getAlbumId(),
                        t.isExplicit(), t.getPlaybackSeconds()))
                .orElse(null);
    }

    public byte[] getCover(String isrc) {
        return repository.findById(isrc)
                .map(t -> {
                    try {
                        return java.nio.file.Files.readAllBytes(java.nio.file.Path.of(t.getCoverPath()));
                    } catch (Exception e) {
                        throw new RuntimeException("Error on image reading", e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Track not found"));
    }

}