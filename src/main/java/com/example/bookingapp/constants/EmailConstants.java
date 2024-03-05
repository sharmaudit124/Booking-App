package com.example.bookingapp.constants;

public class EmailConstants {
    public static final String TO = "sharmaudit124@gmail.com";
    public static final String SUBJECT = "YOUR TICKET IS CONFIRMED";
    public static final String MESSAGE = """
            Dear User,

            Your ticket has been successfully booked!

            Booking Details:
            
            Train Name: %s
            Coach Code: %s
            
            Seat Details:
              - Seat Number: %d
              - Berth: %s
              - Meal Preference: %s
              - Current Price: Rs %.2f

            Thank you for choosing our service.

            Best Regards,
            Your Booking Team
            """;


    private EmailConstants() {
    }
}
