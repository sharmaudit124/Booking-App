package com.example.bookingapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingResponseDTO {
    private String userId;
    private String coachCode;
    private String trainName;
    private SeatDTO seatDetails;
}
