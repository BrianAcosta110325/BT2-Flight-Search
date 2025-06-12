package com.encora.flight_search_be.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value; 
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AmadeusClient {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private String accessToken;
    private long tokenExpiration;

    @Value("${amadeus.api.client-id}")
    private String clientId;

    @Value("${amadeus.api.client-secret}")
    private String clientSecret;

    private final String tokenUrl = "https://test.api.amadeus.com/v1/security/oauth2/token";
    private final String flightSearchUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers";

    public String getAccessToken() {
        if (accessToken == null || System.currentTimeMillis() > tokenExpiration) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "grant_type=client_credentials" +
                    "&client_id=" + clientId +
                    "&client_secret=" + clientSecret;

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                accessToken = (String) responseBody.get("access_token");
                int expiresIn = (int) responseBody.get("expires_in");
                tokenExpiration = System.currentTimeMillis() + (expiresIn * 1000L);
            } else {
                throw new RuntimeException("Error al obtener token de Amadeus");
            }
        }

        return accessToken;
    }

    public ResponseEntity<String> searchFlights(Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        StringBuilder urlBuilder = new StringBuilder(flightSearchUrl + "?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("&");
        }

        String url = urlBuilder.toString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }

    public ResponseEntity<String> searchAirports(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "https://test.api.amadeus.com/v1/reference-data/locations"
                + "?keyword=" + query
                + "&subType=AIRPORT"
                + "&page[limit]=10";

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            String.class
        );
    }
}
