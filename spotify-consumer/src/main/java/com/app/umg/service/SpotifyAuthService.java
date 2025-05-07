package com.app.umg.service;

import com.app.umg.config.SpotifyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SpotifyAuthService {

    private final SpotifyConfig config;

    public String getAccessToken() {
        String credentials = config.getClientId() + ":" + config.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        //TODO: change for feign
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://accounts.spotify.com/api/token",
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            assert responseBody != null;
            return responseBody.split("\"access_token\":\"")[1].split("\"")[0];
        }

        throw new RuntimeException("Token Error: " + response.getStatusCode());
    }
}