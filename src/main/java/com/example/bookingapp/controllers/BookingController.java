package com.example.bookingapp.controllers;

import com.example.bookingapp.dtos.BookingRequestDTO;
import com.example.bookingapp.dtos.BookingResponseDTO;
import com.example.bookingapp.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> bookTicket(@RequestBody BookingRequestDTO bookingRequestDTO) {
        return new ResponseEntity<>(bookingService.bookTrainTicket(bookingRequestDTO), HttpStatus.OK);
    }
}
