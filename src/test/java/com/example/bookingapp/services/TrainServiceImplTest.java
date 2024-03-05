package com.example.bookingapp.services;

import com.example.bookingapp.dtos.CreateTrainDTO;
import com.example.bookingapp.entities.Coach;
import com.example.bookingapp.entities.Seat;
import com.example.bookingapp.entities.Train;
import com.example.bookingapp.repositories.CoachRepository;
import com.example.bookingapp.repositories.SeatRepository;
import com.example.bookingapp.repositories.TrainRepository;
import com.example.bookingapp.services.impl.TrainServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceImplTest {

    @InjectMocks
    private TrainServiceImpl trainService;

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TrainRepository trainRepository;

    @Test
    @DisplayName("Test to create Train")
    void createTrainTest() {
        var createTrain = CreateTrainDTO.builder()
                .trainName("Express")
                .coachCode("A")
                .build();

        when(trainRepository.findByTrainName(any())).thenReturn(Optional.empty());
        when(trainRepository.save(any())).thenReturn(getTrain());
        when(coachRepository.saveAll(any())).thenReturn(getCoaches());

        var res = trainService.createTrain(createTrain);

        verify(trainRepository, times(1)).save(any());
        verify(coachRepository, times(1)).saveAll(any());
        verify(seatRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test to create Train when train exist")
    void createTrainTestWhenExist() {
        var createTrain = CreateTrainDTO.builder()
                .trainName("Express")
                .coachCode("A")
                .build();

        when(trainRepository.findByTrainName(any())).thenReturn(Optional.of(getTrain()));

        assertThrows(IllegalArgumentException.class,
                () -> trainService.createTrain(createTrain));
    }

    @Test
    @DisplayName("test to get train details by name")
    void getTrainDetailsTest() {
        String trainName = "Express";

        when(trainRepository.findByTrainName(any())).thenReturn(Optional.of(getTrain()));
        when(coachRepository.findAllByTrainId(any())).thenReturn(getCoaches());
        when(seatRepository.findAllByCoachIdIn(any())).thenReturn(getSeats());

        assertNotNull(trainService.getTrainDetailsByName(trainName));

    }

    @Test
    @DisplayName("test to get train details by name when not found")
    void getTrainDetailsTestWhenNotFound() {
        String trainName = "Express";

        when(trainRepository.findByTrainName(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> trainService.getTrainDetailsByName(trainName));

    }

    private Train getTrain() {
        return Train.builder()
                .trainId("trainId")
                .trainName("Express")
                .build();
    }

    private List<Coach> getCoaches() {
        return List.of(Coach.builder()
                        .coachId("coachId")
                        .coachCode("A1")
                        .trainId(Train.builder()
                                .trainId("trainId")
                                .build())
                        .basePrice(2000.0)
                        .build(),
                Coach.builder()
                        .coachId("coachId")
                        .coachCode("A2")
                        .trainId(Train.builder()
                                .trainId("trainId")
                                .build())
                        .basePrice(2000.0)
                        .build()
        );
    }

    private List<Seat> getSeats() {
        return List.of(
                Seat.builder()
                        .seatId("123")
                        .coachId(getCoaches().get(0))
                        .seatNo(1)
                        .isBooked(true)
                        .build(),
                Seat.builder()
                        .seatId("234")
                        .coachId(getCoaches().get(1))
                        .seatNo(2)
                        .isBooked(false)
                        .build()
        );
    }
}
