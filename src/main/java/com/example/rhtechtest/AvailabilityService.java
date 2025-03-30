package com.example.rhtechtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AvailabilityService {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private BookingRepository bookingRepository;

    public List<Availability> getAllAvailability(LocalDate start, LocalDate end) {
        var availabilities = new ArrayList<Availability>();

        var bookings = bookingRepository.findAll();

        var propertyBookingMap = bookings.stream()
                .filter(booking -> Objects.nonNull(booking.getProperty()))
                .filter(booking -> Objects.nonNull(booking.getProperty().getId()))
                .collect(groupingBy(booking -> booking.getProperty().getId()));

        var properties = propertyRepository.findAll();

        for (var property : properties) {
            var availabilityStatus = Availability.Status.AVAILABLE;

            if (!propertyBookingMap.containsKey(property.getId())) {
                availabilities.add(new Availability(property.getId(), availabilityStatus));
                continue;
            }

            var propertyBookings = propertyBookingMap.get(property.getId());

            for (var booking : propertyBookings) {
                if (isOverlap(start, end, booking.getCheckIn(), booking.getCheckOut())) {
                    availabilityStatus = Availability.Status.NOT_AVAILABLE;
                    break;
                }
            }

            availabilities.add(new Availability(property.getId(), availabilityStatus));
        }

        return availabilities;
    }

    private boolean isOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        long overlap = Math.min(end1.toEpochDay(), end2.toEpochDay()) -
                Math.max(start1.toEpochDay(), start2.toEpochDay());

        return overlap >= 0;
    }
}
