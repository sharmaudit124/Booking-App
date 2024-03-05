package com.example.bookingapp.controllers;

import com.example.bookingapp.dtos.CreateTrainDTO;
import com.example.bookingapp.dtos.TrainDTO;
import com.example.bookingapp.services.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/train")
public class TrainController {
    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping
    public ResponseEntity<TrainDTO> createTrain(@RequestBody CreateTrainDTO createTrainDTO) {
        return new ResponseEntity<>(trainService.createTrain(createTrainDTO), HttpStatus.OK);
    }

    @GetMapping("/{trainName}")
    public ResponseEntity<TrainDTO> getTrainDetails(@PathVariable String trainName) {
        return new ResponseEntity<>(trainService.getTrainDetailsByName(trainName), HttpStatus.OK);
    }
}
