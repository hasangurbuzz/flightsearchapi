package dev.hasangurbuz.flightsearchapi.client;

import dev.hasangurbuz.flightsearchapi.client.model.FlightResponse;
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
public class MockFlightApiClient implements ApiClient<FlightResponse> {
    private final RestTemplate restTemplate;
    private final String endpoint = "/flights";


    @Override
    public List<FlightResponse> fetch(HttpMethod method, String endpoint) {
        ResponseEntity<List<FlightResponse>> response = restTemplate.exchange(
                endpoint,
                method,
                null,
                new ParameterizedTypeReference<List<FlightResponse>>() {
                });

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return new ArrayList<FlightResponse>();
    }

    @Override
    public List<FlightResponse> get() {
        return fetch(HttpMethod.GET, endpoint);
    }


}
