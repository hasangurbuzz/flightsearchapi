package dev.hasangurbuz.flightsearchapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_airport")
@Getter
@Setter
public class Airport {
    @Id
    @SequenceGenerator(name = "t_airport_seq", sequenceName = "t_airport_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_airport_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "city", nullable = false)
    private String city;


}
