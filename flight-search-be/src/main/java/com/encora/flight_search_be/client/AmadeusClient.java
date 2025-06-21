package com.encora.flight_search_be.client;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
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
    
    private final RestTemplate restTemplate;
    private String accessToken;
    private long tokenExpiration;

    private final static String ACCESS_TOKEN = "access_token";
    private final static String EXPIRES_IN = "expires_in";

    private final static String TOKEN_BODY = "grant_type=client_credentials&client_id={clientId}&client_secret={clientSecret}";

    @Value("${amadeus.api.client-id}")
    private String clientId;

    @Value("${amadeus.api.client-secret}")
    private String clientSecret;

    @Value("${amadeus.api.token-url}")
    private String tokenUrl;

    @Value("${amadeus.api.flight-search-url}")
    private String flightSearchUrl;

    @Value("${amadeus.api.search-airports-url}")
    private String searchAriportsUrl;
  
    @Value("${amadeus.api.search-airlines-url}")
    private String airlineSearchUrl;

    public AmadeusClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            params.forEach((key, value) -> 
                urlBuilder.append(key)
                          .append("=")
                          .append(value)
                          .append("&")
            );
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }    

    public String getAccessToken() {
        if (accessToken!=null && System.currentTimeMillis() < tokenExpiration) {
            return accessToken;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = getTockenBody();

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                Map.class
        );
        
        if (response.getStatusCode() != HttpStatus.OK) {
          throw new RuntimeException("Error obtaining Amadeus token");
        }
      
        Map<String, Object> responseBody = response.getBody();
        accessToken = (String) responseBody.get(ACCESS_TOKEN);
        int expiresIn = (int) responseBody.get(EXPIRES_IN);
        tokenExpiration = System.currentTimeMillis() + (expiresIn * 1000L);

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
        String url = buildUrlWithParams(flightSearchUrl, params);

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
        HttpHeaders headers = getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> params = new HashMap<>();
        params.put("keyword", query);
        params.put("subType", "AIRPORT");
        params.put("page[limit]", "10");

        String url = buildUrlWithParams(searchAriportsUrl, params);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            String.class
        );
    }

    @Cacheable(value = "airportByCode", key = "#code", condition = "#code != null")
    public String searchAirportByCode(String code) {
        if (code == null) return "";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = searchAriportsUrl + "?keyword=" + code + "&subType=AIRPORT,CITY&view=FULL";
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            JsonNode.class
        );

        JsonNode data = response.getBody().path("data");
        if (data.isArray() && data.size() > 0) {
            return data.get(0).path("name").asText();
        }

        return "";
    }

    @Cacheable(value = "airlineByCode", key = "#code", condition = "#code != null")
    public String searchAirlineByCode(String code) {
        if (code == null || code.isEmpty()) {
            return "";
        }
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

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Airport not found with code: " + code);
        }

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("data")) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) responseBody.get("data");
            if (!data.isEmpty()) {
                return (String) data.get(0).get("name");
            }
        }
        return "";
    }
  
    private String getTockenBody () {
        return TOKEN_BODY.replace("{clientId}", clientId)
        .replace("{clientSecret}", clientSecret);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        return headers;
    }
}
