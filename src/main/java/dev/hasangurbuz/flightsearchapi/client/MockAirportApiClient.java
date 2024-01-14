package dev.hasangurbuz.flightsearchapi.client;

import dev.hasangurbuz.flightsearchapi.client.model.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockAirportApiClient implements ApiClient<AirportResponse> {

    private final RestTemplate restTemplate;
    private final String endpoint = "/airports";

    @Override
    public List<AirportResponse> fetch(HttpMethod method, String endpoint) {
        ResponseEntity<List<AirportResponse>> response = restTemplate.exchange(
                endpoint,
                method,
                null,
                new ParameterizedTypeReference<List<AirportResponse>>() {
                });

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return new ArrayList<AirportResponse>();
    }

    @Override
    public List<AirportResponse> get() {
        return fetch(HttpMethod.GET, endpoint);
    }

}
