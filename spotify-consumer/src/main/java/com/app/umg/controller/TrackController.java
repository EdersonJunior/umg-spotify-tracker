package com.app.umg.controller;

import com.app.umg.dto.TrackDTO;
import com.app.umg.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService service;
    private static final Logger log = LoggerFactory.getLogger(TrackController.class);

    @PostMapping("/createTrack")
    public ResponseEntity<Void> createTrack(@RequestParam String isrc) {
        log.info("Creating new track with the ISRC {}", isrc);
        service.createTrackByIsrc(isrc);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getTrackMetadata")
    public ResponseEntity<TrackDTO> getTrackMetadata(@RequestParam String isrc) {
        log.info("Searching with the ISRC {}", isrc);
        return ResponseEntity.ok(service.getMetadata(isrc));
    }

//    USED IF BYTE CODE WANTED
//    @GetMapping("/getCover")
//    public ResponseEntity<byte[]> getCover(@RequestParam String isrc) {
//        return ResponseEntity.ok(service.getCover(isrc));
//    }

    @GetMapping("/getCover")
    public ResponseEntity<byte[]> getCover(@RequestParam String isrc) {
        byte[] image = service.getCover(isrc);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

}
