package com.example.bookingapp.repositories;

import com.example.bookingapp.entities.Seat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends MongoRepository<Seat, String> {
    List<Seat> findAllByCoachIdIn(List<String> coachIds);

    List<Seat> findAllByCoachId(String coachId);
}
