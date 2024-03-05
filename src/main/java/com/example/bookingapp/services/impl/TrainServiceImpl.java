package com.example.bookingapp.services.impl;

import com.example.bookingapp.dtos.CoachDTO;
import com.example.bookingapp.dtos.CreateTrainDTO;
import com.example.bookingapp.dtos.SeatDTO;
import com.example.bookingapp.dtos.TrainDTO;
import com.example.bookingapp.entities.Coach;
import com.example.bookingapp.entities.Seat;
import com.example.bookingapp.entities.Train;
import com.example.bookingapp.enums.Berth;
import com.example.bookingapp.repositories.CoachRepository;
import com.example.bookingapp.repositories.SeatRepository;
import com.example.bookingapp.repositories.TrainRepository;
import com.example.bookingapp.services.TrainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.bookingapp.constants.ErrorConstants.TRAIN_EXIST_MSG;
import static com.example.bookingapp.constants.ErrorConstants.TRAIN_NOT_FOUND_MSG;
import static com.example.bookingapp.constants.TrainConstants.*;

@Service
public class TrainServiceImpl implements TrainService {
    private final CoachRepository coachRepository;
    private final SeatRepository seatRepository;
    private final TrainRepository trainRepository;
    private final Queue<Berth> q = new ArrayDeque<>();

    public TrainServiceImpl(CoachRepository coachRepository, SeatRepository seatRepository, TrainRepository trainRepository) {
        this.coachRepository = coachRepository;
        this.seatRepository = seatRepository;
        this.trainRepository = trainRepository;
    }

    @Override
    @Transactional
    public TrainDTO getTrainDetailsByName(String trainName) {
        var train = trainRepository.findByTrainName(trainName)
                .orElseThrow(() -> new IllegalArgumentException(TRAIN_NOT_FOUND_MSG));

        var coaches = coachRepository.findAllByTrainId(train.getTrainId());
        List<CoachDTO> coachDTOS = new ArrayList<>(coaches.size());

        List<Seat> allSeats = seatRepository.findAllByCoachIdIn(coaches.stream()
                .map(Coach::getCoachId)
                .collect(Collectors.toList()));

        Map<String, List<Seat>> seatsByCoachId = allSeats.stream()
                .collect(Collectors.groupingBy(seat -> seat.getCoachId().getCoachId()));

        for (Coach coach : coaches) {
            List<Seat> seats = seatsByCoachId
                    .getOrDefault(coach.getCoachId(), Collections.emptyList());

            coachDTOS.add(CoachDTO.builder()
                    .coachCode(coach.getCoachCode())
                    .coachId(coach.getCoachId())
                    .basePrice(coach.getBasePrice())
                    .seats(seats.stream()
                            .map(this::mapToSeatDTO)
                            .collect(Collectors.toList()))
                    .build());
        }

        return TrainDTO.builder()
                .trainName(train.getTrainName())
                .trainId(train.getTrainId())
                .coaches(coachDTOS)
                .build();
    }

    @Override
    @Transactional
    public TrainDTO createTrain(CreateTrainDTO createTrainDTO) {

        if (trainRepository.findByTrainName(createTrainDTO.getTrainName()).isPresent()) {
            throw new IllegalArgumentException(TRAIN_EXIST_MSG);
        }

        var train = trainRepository.save(Train.builder()
                .trainName(createTrainDTO.getTrainName())
                .build());

        var coaches = coachRepository.saveAll(createCoach(train,
                createTrainDTO.getCoachCode()));

        List<Seat> seats = new ArrayList<>();
        List<CoachDTO> coachDTOS = new ArrayList<>(coaches.size());

        for (Coach coach : coaches) {
            var createdSeats = createSeat(coach);
            seats.addAll(createdSeats);

            coachDTOS.add(CoachDTO.builder()
                    .coachCode(coach.getCoachCode())
                    .coachId(coach.getCoachId())
                    .basePrice(BASE_PRICE)
                    .seats(createdSeats.stream()
                            .map(this::mapToSeatDTO)
                            .collect(Collectors.toList()))
                    .build());
        }

        seatRepository.saveAll(seats);

        return TrainDTO.builder()
                .trainId(train.getTrainId())
                .trainName(train.getTrainName())
                .coaches(coachDTOS)
                .build();
    }

    private List<Coach> createCoach(Train train, String coachCode) {
        if (q.isEmpty()) {
            q.add(Berth.LOWER);
            q.add(Berth.MIDDLE);
            q.add(Berth.UPPER);
        }
        List<Coach> coaches = new ArrayList<>();
        for (int i = 1; i <= TOTAL_COACHES; i++) {
            coaches.add(Coach.builder()
                    .coachCode(coachCode + i)
                    .basePrice(BASE_PRICE)
                    .trainId(train)
                    .build());
        }
        return coaches;
    }

    private List<Seat> createSeat(Coach coach) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= TOTAL_SEATS; i++) {
            seats.add(Seat.builder()
                    .seatNo(i)
                    .coachId(coach)
                    .berth(getCurrentBerth())
                    .currentPrice(BASE_PRICE)
                    .isBooked(false)
                    .build()
            );
        }
        return seats;
    }

    private SeatDTO mapToSeatDTO(Seat seat) {
        return SeatDTO.builder()
                .seatNo(seat.getSeatNo())
                .seatId(seat.getSeatId())
                .berth(seat.getBerth())
                .mealPreference(seat.getMealPreference())
                .currentPrice(seat.getCurrentPrice())
                .booked(seat.getIsBooked())
                .build();
    }

    private Berth getCurrentBerth() {
        var b = q.remove();
        q.add(b);
        return b;
    }
}
