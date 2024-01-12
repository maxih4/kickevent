package com.example.kickevent.security;

import com.example.kickevent.exceptions.JwtTokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);

        } catch (JwtTokenExpiredException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



}