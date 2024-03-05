package com.example.bookingapp.repositories;

import com.example.bookingapp.entities.Coach;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends MongoRepository<Coach, String> {
    List<Coach> findAllByTrainId(String trainId);
}
