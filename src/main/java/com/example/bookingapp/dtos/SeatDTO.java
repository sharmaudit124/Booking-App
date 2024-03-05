package com.example.bookingapp.dtos;

import com.example.bookingapp.enums.Berth;
import com.example.bookingapp.enums.MealPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SeatDTO {
    private String seatId;
    private int seatNo;
    private Berth berth;
    private MealPreference mealPreference;
    private Double currentPrice;
    private boolean booked;
}
