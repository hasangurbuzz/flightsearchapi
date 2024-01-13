package dev.hasangurbuz.flightsearchapi.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedResult<T> {
    private int total;
    private List<T> items;
}
