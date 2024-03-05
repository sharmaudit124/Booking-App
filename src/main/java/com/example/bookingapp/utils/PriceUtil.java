package com.example.bookingapp.utils;

import com.example.bookingapp.entities.Seat;
import com.example.bookingapp.repositories.CoachRepository;
import com.example.bookingapp.repositories.SeatRepository;
import com.example.bookingapp.strategy.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.bookingapp.constants.ErrorConstants.COACH_NOT_FOUND_MSG;

@Component
public class PriceUtil {
    private final CoachRepository coachRepository;
    private final SeatRepository seatRepository;

    public PriceUtil(CoachRepository coachRepository, SeatRepository seatRepository) {
        this.coachRepository = coachRepository;
        this.seatRepository = seatRepository;
    }

    public void updatePrice(String coachId) {
        var coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalArgumentException(COACH_NOT_FOUND_MSG));

        List<Seat> seats = seatRepository.findAllByCoachId(coachId);

        int totalSeats = seats.size();
        int filledSeats = (int) seats.stream().filter(Seat::getIsBooked).count();

        if (filledSeats == 0) {
            return;
        }

        double fillPercentage = (double) filledSeats / totalSeats * 100.0;
        PriceStrategy pricingStrategy = getPricingStrategy(fillPercentage);

        for (Seat seat : seats) {
            if (Boolean.TRUE.equals(seat.getIsBooked())) continue;
            if (pricingStrategy != null) {
                double priceIncrease = pricingStrategy.calculatePriceIncrease(fillPercentage, coach.getBasePrice());
                double finalPrice = coach.getBasePrice() + priceIncrease;
                seat.setCurrentPrice(finalPrice);
            } else {
                seat.setCurrentPrice(coach.getBasePrice());
            }
        }

        seatRepository.saveAll(seats);
    }

    private PriceStrategy getPricingStrategy(double fillPercentage) {
        if (fillPercentage >= 20 && fillPercentage < 30) {
            return new TenPercentStrategy();
        } else if (fillPercentage >= 30 && fillPercentage < 35) {
            return new TwelvePercentStrategy();
        } else if (fillPercentage >= 35 && fillPercentage < 40) {
            return new FifteenPercentStrategy();
        } else if (fillPercentage >= 40) {
            return new EighteenPercentStrategy();
        }
        return null;
    }
}
