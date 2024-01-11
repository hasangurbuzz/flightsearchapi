package dev.hasangurbuz.flightsearchapi.repository;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
}
