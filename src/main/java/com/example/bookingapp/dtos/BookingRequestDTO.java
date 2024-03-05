package com.example.bookingapp.dtos;

import com.example.bookingapp.enums.Berth;
import com.example.bookingapp.enums.MealPreference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingRequestDTO {
    private String userId;
    private String trainName;
    @Schema(description = "Meal preference", implementation = MealPreference.class)
    private String mealPreference;
    @Schema(description = "Berth preference", implementation = Berth.class)
    private String berthPreference;
}
