package dev.hasangurbuz.flightsearchapi.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.client.MockAirportApiClient;
import dev.hasangurbuz.flightsearchapi.client.MockFlightApiClient;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, @Value("${mock.api.url}") String basePath) {
        return builder.rootUri(basePath).build();
    }

    @Bean
    public MockAirportApiClient mockAirportApiClient(RestTemplate restTemplate) {
        return new MockAirportApiClient(restTemplate);
    }

    @Bean
    public MockFlightApiClient mockFlightApiClient(RestTemplate restTemplate) {
        return new MockFlightApiClient(restTemplate);
    }

}
