package com.encora.flight_search_be.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchFlightResponseDto {
    private List<FlightSearchResponseDto> flights;
    private int totalPages;
}
