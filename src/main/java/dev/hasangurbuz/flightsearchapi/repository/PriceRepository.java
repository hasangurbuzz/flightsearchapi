package dev.hasangurbuz.flightsearchapi.repository;

import dev.hasangurbuz.flightsearchapi.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
}
