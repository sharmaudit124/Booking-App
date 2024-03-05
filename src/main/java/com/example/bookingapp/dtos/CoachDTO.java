package com.example.bookingapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CoachDTO {
    private String coachId;
    private String coachCode;
    private Double basePrice;
    private List<SeatDTO> seats;
}
