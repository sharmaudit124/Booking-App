package com.example.bookingapp.entities;

import com.example.bookingapp.enums.Berth;
import com.example.bookingapp.enums.MealPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Seat {
    @Id
    private String seatId;
    @DBRef
    private Coach coachId;
    private int seatNo;
    private Berth berth;
    private MealPreference mealPreference;
    private Double currentPrice;
    private Boolean isBooked;

}
