package com.example.bookingapp.strategy;

public interface PriceStrategy {
    double calculatePriceIncrease(double fillPercentage, double basePrice);
}
