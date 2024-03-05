package com.example.bookingapp.services;

import com.example.bookingapp.dtos.BookingRequestDTO;
import com.example.bookingapp.dtos.CoachDTO;
import com.example.bookingapp.dtos.SeatDTO;
import com.example.bookingapp.dtos.TrainDTO;
import com.example.bookingapp.entities.Coach;
import com.example.bookingapp.enums.Berth;
import com.example.bookingapp.enums.MealPreference;
import com.example.bookingapp.repositories.BookingRepository;
import com.example.bookingapp.repositories.CoachRepository;
import com.example.bookingapp.repositories.SeatRepository;
import com.example.bookingapp.services.impl.BookingServiceImpl;
import com.example.bookingapp.utils.EmailUtils;
import com.example.bookingapp.utils.PriceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private TrainService trainService;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private CoachRepository coachRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private EmailUtils emailUtils;
    @Mock
    private PriceUtil priceUtil;

    @Test
    @DisplayName("Test to book train ticket")
    void bookTrainTicket() {
        var bookingRequest = BookingRequestDTO.builder()
                .trainName("Express")
                .mealPreference("VEG")
                .berthPreference("Upper")
                .userId("1235235")
                .build();

        when(trainService.getTrainDetailsByName(any())).thenReturn(getTrainDTOs());
        when(coachRepository.findById(any())).thenReturn(Optional.of(getCoach()));

        bookingService.bookTrainTicket(bookingRequest);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test to book train ticket when coach not found")
    void bookTrainTicketWhenCoachNotFound() {
        var bookingRequest = BookingRequestDTO.builder()
                .trainName("Express")
                .mealPreference("VEG")
                .berthPreference("Upper")
                .userId("1235235")
                .build();

        when(trainService.getTrainDetailsByName(any())).thenReturn(getTrainDTOs());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.bookTrainTicket(bookingRequest));

    }

    private TrainDTO getTrainDTOs() {
        return TrainDTO.builder()
                .trainId("45436653")
                .coaches(getCoachDTOs())
                .trainName("Express")
                .build();
    }

    private List<CoachDTO> getCoachDTOs() {
        return Arrays.asList(
                CoachDTO.builder()
                        .basePrice(5000.0)
                        .coachId("87585")
                        .coachCode("A1")
                        .seats(getSeatDTOs())
                        .build()
        );
    }

    private Coach getCoach() {
        return Coach.builder()
                .basePrice(5000.0)
                .coachId("87585")
                .coachCode("A1")
                .build();
    }

    private List<SeatDTO> getSeatDTOs() {
        return Arrays.asList(
                SeatDTO.builder()
                        .seatId("1234")
                        .seatNo(1)
                        .currentPrice(2000.0)
                        .mealPreference(MealPreference.VEG)
                        .berth(Berth.LOWER)
                        .booked(true)
                        .build(),

                SeatDTO.builder()
                        .seatId("1234")
                        .seatNo(2)
                        .currentPrice(2040.0)
                        .mealPreference(MealPreference.NON_VEG)
                        .berth(Berth.LOWER)
                        .booked(false)
                        .build()
        );
    }
}
