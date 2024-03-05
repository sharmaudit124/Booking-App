package com.example.bookingapp.utils;

import com.example.bookingapp.entities.Coach;
import com.example.bookingapp.entities.Seat;
import com.example.bookingapp.entities.Train;
import com.example.bookingapp.repositories.CoachRepository;
import com.example.bookingapp.repositories.SeatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceUtilsTest {

    @InjectMocks
    private PriceUtil priceUtil;

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private SeatRepository seatRepository;

    @Test
    @DisplayName("Test to update Price")
    void updatePriceTest() {

        when(coachRepository.findById(any())).thenReturn(Optional.of(getCoach()));
        when(seatRepository.findAllByCoachId(any())).thenReturn(getSeats());

        priceUtil.updatePrice("coachId");

        verify(seatRepository, times(1)).saveAll(any());

    }

    @Test
    @DisplayName("Test to update Price when coach is null")
    void updatePriceTestWhenCoachNull() {
        when(coachRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> priceUtil.updatePrice("coachId"));
    }

    private Coach getCoach() {
        return Coach.builder()
                .coachId("coachId")
                .coachCode("A1")
                .trainId(Train.builder()
                        .trainId("trainId")
                        .build())
                .basePrice(2000.0)
                .build();
    }

    private List<Seat> getSeats() {
        return List.of(
                Seat.builder()
                        .seatId("123")
                        .coachId(getCoach())
                        .seatNo(1)
                        .isBooked(true)
                        .build(),
                Seat.builder()
                        .seatId("234")
                        .coachId(getCoach())
                        .seatNo(2)
                        .isBooked(false)
                        .build()
        );
    }

}
