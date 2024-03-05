package com.example.bookingapp.dtos;

import java.time.LocalDateTime;

public record ErrorDTO(int code, String error, LocalDateTime timestamp) {
}
