package com.example.bookingapp.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.bookingapp.constants.AdminConstants.ADMIN_PASSWORD;
import static com.example.bookingapp.constants.AdminConstants.ADMIN_USERNAME;

@Slf4j
public class BasicAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Checking whether admin accessing...");
        String userNameHeader = request.getHeader("userName");
        String passwordHeader = request.getHeader("password");

        if (userNameHeader != null && passwordHeader != null && userNameHeader.equals(ADMIN_USERNAME) && passwordHeader.equals(ADMIN_PASSWORD)) {
            return true;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String errorMessage = "{\"code\": " + HttpStatus.UNAUTHORIZED.value() + ", \"message\": \"Access Denied\"}";

        response.getWriter().write(errorMessage);

        return false;
    }
}
