package com.app.umg.service;

import com.app.umg.dto.TrackDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final SpotifyAuthService authService;

    public TrackDTO getTrackByIsrc(String isrc) {
        log.info("Search metadata with the following ISRC: {}", isrc);

        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/search?q=isrc:" + isrc + "&type=track";

        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode item = root.path("tracks").path("items").get(0);

                return new TrackDTO(
                        isrc,
                        item.path("name").asText(),
                        item.path("artists").get(0).path("name").asText(),
                        item.path("album").path("name").asText(),
                        item.path("album").path("id").asText(),
                        item.path("explicit").asBoolean(),
                        item.path("duration_ms").asInt() / 1000
                );
            } catch (Exception e) {
                throw new RuntimeException("Error", e);
            }
        }

        throw new RuntimeException("Error: " + response.getStatusCode());
    }

    public String downloadAlbumCover(String albumId, String isrc) {
        String token = authService.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "https://api.spotify.com/v1/albums/" + albumId;

        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error at search album: " + response.getStatusCode());
        }

        try {
            String json = response.getBody();
            String imageUrl = json.split("\"url\":\"")[1].split("\"")[0];

            Path savePath = Paths.get("covers", isrc + ".jpg");
            savePath.toFile().getParentFile().mkdirs();

            try (InputStream in = new URL(imageUrl).openStream();
                 FileOutputStream out = new FileOutputStream(savePath.toFile())) {
                in.transferTo(out);
            }

            log.info("Cover downloaded {}", savePath.toAbsolutePath());
            return savePath.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error at download cover", e);
        }
    }
}