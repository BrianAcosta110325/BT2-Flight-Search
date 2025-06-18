package com.encora.flight_search_be.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AmadeusClient {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private String accessToken;
    private long tokenExpiration;

    @Value("${amadeus.api.client-id}")
    private String clientId;

    @Value("${amadeus.api.client-secret}")
    private String clientSecret;

    private final String baseUrl = "https://test.api.amadeus.com";
    private final String tokenUrl = baseUrl + "/v1/security/oauth2/token";
    private final String flightSearchUrl = baseUrl + "/v2/shopping/flight-offers";
    private final String airportSearchUrl = baseUrl + "/v1/reference-data/locations";
    private final String airlineSearchUrl = baseUrl + "/v1/reference-data/airlines?";

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
                throw new RuntimeException("Error obtaining Amadeus token");
            }
        }

        return accessToken;
    }

    @Cacheable(value = "flightSearch", key = "#params.toString()")
    public JsonNode searchFlights(Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        StringBuilder urlBuilder = new StringBuilder(flightSearchUrl + "?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        // Elimina el último '&' si está
        String url = urlBuilder.substring(0, urlBuilder.length() - 1);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data");
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RuntimeException("Error searching flights: " + e.getMessage(), e);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error calling Amadeus API: " + e.getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON response: " + e.getMessage(), e);
        }
    }


    public ResponseEntity<String> searchAirports(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = airportSearchUrl
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

    @Cacheable(value = "airportByCode", key = "#code")
    public String searchAirportByCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = airportSearchUrl + "?subType=AIRPORT&keyword=" + code;

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");
                if (!data.isEmpty()) {
                    return (String) data.get(0).get("name");
                }
            }
        }

        return "";
    }

    @Cacheable(value = "airlineByCode", key = "#code")
    public String searchAirlineByCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = airlineSearchUrl + "airlineCodes=" + code;

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");
                if (!data.isEmpty()) {
                    return (String) data.get(0).get("businessName");
                }
            }
        }

        return "";
    }
}
