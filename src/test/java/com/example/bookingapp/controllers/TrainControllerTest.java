package com.example.bookingapp.controllers;

import com.example.bookingapp.dtos.CreateTrainDTO;
import com.example.bookingapp.dtos.TrainDTO;
import com.example.bookingapp.services.TrainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainControllerTest {

    @InjectMocks
    private TrainController trainController;

    @Mock
    private TrainService trainService;

    @Test
    @DisplayName("Test to create train")
    void createTrainTest() {
        var createDto = CreateTrainDTO.builder().build();
        var dto = TrainDTO.builder().build();

        when(trainService.createTrain(any())).thenReturn(dto);

        var res = trainController.createTrain(createDto);

        assertEquals(res.getBody(), dto);
    }

    @Test
    @DisplayName("Test to get train by trainName")
    void getTrainTest() {
        String trainName = "Express";
        var dto = TrainDTO.builder().build();

        when(trainService.getTrainDetailsByName(any())).thenReturn(dto);

        var res = trainController.getTrainDetails(trainName);

        assertEquals(res.getBody(), dto);
    }
}
