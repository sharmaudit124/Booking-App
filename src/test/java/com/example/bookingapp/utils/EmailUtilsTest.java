package com.example.bookingapp.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailUtilsTest {

    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private EmailUtils emailUtils;

    @Test
    @DisplayName("Test sending email")
    void testSendEmail() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        emailUtils.sendEmail(to, subject, message);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Test sending email failure")
    void testSendEmailFailure() {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        doThrow(new MailSendException("Failed to send email")).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailUtils.sendEmail(to, subject, message);

    }
}

