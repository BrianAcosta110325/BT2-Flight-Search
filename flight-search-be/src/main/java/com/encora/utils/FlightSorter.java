package com.encora.utils;

import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto;
import java.util.Comparator;
import java.util.List;

public class FlightSorter {

    public static void sort(List<FlightSearchAmadeusResposeDto> flights, Sorter sortBy) {
        if (sortBy == null) return;
        
        switch (sortBy) {
            case basePrice:
                flights.sort(Comparator.comparing(f ->
                    Double.parseDouble(f.getPrice().getBase())
                ));
                break;
            case totalPrice:
                flights.sort(Comparator.comparing(f ->
                    Double.parseDouble(f.getPrice().getTotal())
                ));
                break;
            case pricePerTraveler:
                flights.sort(Comparator.comparing(f ->
                    Double.parseDouble(f.getTravelerPricings().get(0).getPrice().getTotal())
                ));
                break;
            case fees:
                flights.sort(Comparator.comparing(f -> {
                    double totalFees = f.getPrice().getFees().stream()
                        .mapToDouble(fee -> Double.parseDouble(fee.getAmount()))
                        .sum();
                    return totalFees;
                }));
                break;
        }
    }
}
