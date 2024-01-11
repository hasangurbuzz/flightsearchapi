package dev.hasangurbuz.flightsearchapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

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
    @JoinColumn(name = "origin_airport_id", nullable = false)
    private Airport origin;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id", nullable = false)
    private Airport destination;

    @Column(name = "departure_date", nullable = false)
    private OffsetDateTime departureDate;

    @Column(name = "return_date", nullable = true)
    private OffsetDateTime returnDate;

    @OneToOne
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;


}
