package com.example.bookingapp.services;

import com.example.bookingapp.dtos.BookingRequestDTO;
import com.example.bookingapp.dtos.BookingResponseDTO;

public interface BookingService {
    BookingResponseDTO bookTrainTicket(BookingRequestDTO bookingRequestDTO);
}
