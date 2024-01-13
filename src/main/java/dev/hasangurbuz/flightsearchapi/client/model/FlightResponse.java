package dev.hasangurbuz.flightsearchapi.client.model;

import lombok.Data;

@Data
public class FlightResponse {
    private Integer id;
    private Integer origin;
    private Integer destination;
    private String price;
    private String departureDate;
    private String returnDate;
    private String currency;
}
