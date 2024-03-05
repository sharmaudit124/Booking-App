package com.example.bookingapp.strategy;

public class TwelvePercentStrategy implements PriceStrategy {
    @Override
    public double calculatePriceIncrease(double fillPercentage, double basePrice) {
        return basePrice * 0.12;
    }
}
