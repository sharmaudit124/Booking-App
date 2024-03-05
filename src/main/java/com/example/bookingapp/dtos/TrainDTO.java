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
public class TrainDTO {
    private String trainId;
    private String trainName;
    private List<CoachDTO> coaches;
}
