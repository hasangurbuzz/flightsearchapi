package dev.hasangurbuz.flightsearchapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

@Entity
@Table(name = "t_flight")
@Getter
@Setter
public class Flight {
    @Id
    @SequenceGenerator(name = "t_flight_seq", sequenceName = "t_flight_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_flight_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    @Column(name = "departure_date")
    private OffsetDateTime departureDate;

    @Column(name = "return_date")
    private OffsetDateTime returnDate;

    @Column(name = "price")
    private BigDecimal price;

}
