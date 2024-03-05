package com.example.bookingapp.services;

import com.example.bookingapp.dtos.CreateTrainDTO;
import com.example.bookingapp.dtos.TrainDTO;

public interface TrainService {
    TrainDTO createTrain(CreateTrainDTO createTrainDTO);

    TrainDTO getTrainDetailsByName(String trainName);
}
