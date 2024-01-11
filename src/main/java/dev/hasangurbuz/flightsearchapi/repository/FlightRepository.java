package dev.hasangurbuz.flightsearchapi.repository;

import dev.hasangurbuz.flightsearchapi.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
