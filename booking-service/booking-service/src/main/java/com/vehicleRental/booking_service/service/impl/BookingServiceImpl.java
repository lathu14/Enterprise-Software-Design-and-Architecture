package com.vehicleRental.booking_service.service.impl;

import com.vehicleRental.booking_service.model.Booking;
import com.vehicleRental.booking_service.repository.BookingRepository;
import com.vehicleRental.booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking createBooking(Booking booking) {

        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    @Override
    public Booking updateBooking(Long id, Booking booking) {

        Booking existingBooking =
                bookingRepository.findById(id).orElseThrow();

        existingBooking.setStatus(booking.getStatus());

        return bookingRepository.save(existingBooking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}