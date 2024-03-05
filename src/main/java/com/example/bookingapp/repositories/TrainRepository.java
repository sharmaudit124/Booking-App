package com.example.bookingapp.repositories;

import com.example.bookingapp.entities.Train;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainRepository extends MongoRepository<Train, String> {
    @Query("{ 'trainName' : { $regex: ?0, $options: 'i' } }")
    Optional<Train> findByTrainName(String trainName);
}
