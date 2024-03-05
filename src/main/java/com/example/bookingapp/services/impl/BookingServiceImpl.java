package com.example.bookingapp.services.impl;

import com.example.bookingapp.dtos.BookingRequestDTO;
import com.example.bookingapp.dtos.BookingResponseDTO;
import com.example.bookingapp.dtos.CoachDTO;
import com.example.bookingapp.dtos.SeatDTO;
import com.example.bookingapp.entities.Booking;
import com.example.bookingapp.entities.Coach;
import com.example.bookingapp.entities.Seat;
import com.example.bookingapp.enums.MealPreference;
import com.example.bookingapp.repositories.BookingRepository;
import com.example.bookingapp.repositories.CoachRepository;
import com.example.bookingapp.repositories.SeatRepository;
import com.example.bookingapp.services.BookingService;
import com.example.bookingapp.services.TrainService;
import com.example.bookingapp.utils.EmailUtils;
import com.example.bookingapp.utils.PriceUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.bookingapp.constants.EmailConstants.*;
import static com.example.bookingapp.constants.ErrorConstants.COACH_NOT_FOUND_MSG;
import static com.example.bookingapp.constants.ErrorConstants.TRAIN_FULL_MSG;

@Service
public class BookingServiceImpl implements BookingService {
    private final TrainService trainService;
    private final SeatRepository seatRepository;
    private final CoachRepository coachRepository;
    private final BookingRepository bookingRepository;
    private final EmailUtils emailUtils;
    private final PriceUtil priceUtil;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public BookingServiceImpl(TrainService trainService, SeatRepository seatRepository, CoachRepository coachRepository, BookingRepository bookingRepository, EmailUtils emailUtils, PriceUtil priceUtil) {
        this.trainService = trainService;
        this.seatRepository = seatRepository;
        this.coachRepository = coachRepository;
        this.bookingRepository = bookingRepository;
        this.emailUtils = emailUtils;
        this.priceUtil = priceUtil;
    }

    @Override
    @Transactional
    public BookingResponseDTO bookTrainTicket(BookingRequestDTO bookingRequestDTO) {

        var train = trainService.getTrainDetailsByName(bookingRequestDTO.getTrainName());
        List<CoachDTO> coaches = sortCoaches(train.getCoaches());

        var val = coaches.stream()
                .map(coach -> coach.getSeats().stream()
                        .filter(seat -> !seat.isBooked())
                        .filter(seat -> bookingRequestDTO.getBerthPreference().equalsIgnoreCase(seat.getBerth().name()))
                        .findFirst()
                        .map(seat -> new Object[]{coach.getCoachCode(), coach.getCoachId(), seat})
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
        if (val.isEmpty()) {
            val =
                    coaches.stream()
                            .map(coach -> coach.getSeats().stream()
                                    .filter(seat -> !seat.isBooked())
                                    .findFirst()
                                    .map(seat -> new Object[]{coach.getCoachCode(), coach.getCoachId(), seat})
                            )
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .findFirst();
        }
        String coachCode = "";
        SeatDTO dto = null;
        String coachId = "";

        if (val.isPresent()) {
            var arr = val.get();
            coachCode = (String) arr[0];
            coachId = (String) arr[1];
            dto = (SeatDTO) arr[2];
        } else {
            throw new IllegalArgumentException(TRAIN_FULL_MSG);
        }

        assert dto != null;
        dto.setMealPreference(MealPreference.valueOf(bookingRequestDTO.getMealPreference()));
        dto.setBooked(true);

        var optCoach = coachRepository.findById(coachId);

        Coach coach = null;
        if (optCoach.isPresent()) {
            coach = optCoach.get();
        } else {
            throw new IllegalArgumentException(COACH_NOT_FOUND_MSG);
        }

        var seat = seatRepository.save(Seat.builder()
                .seatId(dto.getSeatId())
                .coachId(coach)
                .seatNo(dto.getSeatNo())
                .berth(dto.getBerth())
                .mealPreference(dto.getMealPreference())
                .currentPrice(dto.getCurrentPrice())
                .isBooked(dto.isBooked())
                .build());

        var booking = Booking.builder()
                .bookedAt(LocalDateTime.now())
                .userId(bookingRequestDTO.getUserId())
                .seat(seat)
                .build();

        bookingRepository.save(booking);

        priceUtil.updatePrice(coachId);

        Coach finalCoach = coach;
        executorService.submit(() -> emailUtils.sendEmail(TO, SUBJECT,
                String.format(MESSAGE, train.getTrainName(), finalCoach.getCoachCode(), seat.getSeatNo(), seat.getBerth(),
                        seat.getMealPreference(), seat.getCurrentPrice())
        ));

        return BookingResponseDTO.builder()
                .coachCode(coachCode)
                .trainName(train.getTrainName())
                .seatDetails(dto)
                .userId(booking.getUserId())
                .build();
    }

    private List<CoachDTO> sortCoaches(List<CoachDTO> coaches) {
        coaches.sort(Comparator.comparingInt(coach -> {
            int coachNumber = Integer.parseInt(coach.getCoachCode().substring(1));
            return coachNumber % 2 == 0 ? Integer.MAX_VALUE : coachNumber;
        }));
        return coaches;
    }

}
