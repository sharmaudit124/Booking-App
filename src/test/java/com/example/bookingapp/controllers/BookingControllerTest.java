package com.example.bookingapp.controllers;

import com.example.bookingapp.dtos.BookingRequestDTO;
import com.example.bookingapp.dtos.BookingResponseDTO;
import com.example.bookingapp.services.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Test
    @DisplayName("Test for book ticket")
    void bookTicketTest() {
        var dto = BookingRequestDTO.builder().build();
        var res = BookingResponseDTO.builder().build();

        when(bookingService.bookTrainTicket(any())).thenReturn(res);

        var result = bookingController.bookTicket(dto);
        assertEquals(result.getBody(), res);
    }
}
