package com.example.bookingapp.strategy;

public class TenPercentStrategy implements PriceStrategy {
    @Override
    public double calculatePriceIncrease(double fillPercentage, double basePrice) {
        return basePrice * 0.10;
    }
}
