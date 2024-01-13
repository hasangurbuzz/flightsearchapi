package dev.hasangurbuz.flightsearchapi.client;

import org.springframework.http.HttpMethod;

import java.util.List;

public interface ApiClient<T> {

    List<T> fetch(HttpMethod method, String endpoint);

    List<T> get();

}
