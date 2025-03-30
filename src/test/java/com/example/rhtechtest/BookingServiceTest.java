package com.example.rhtechtest;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        var expectedBookings = generateTestBookings();

        given(bookingRepository.findAll())
                .willReturn(expectedBookings);
    }

    @Test
    void whenGetAllBookings_thenCallRepository() {
        bookingService.getAllBookings();

        BDDMockito.then(bookingRepository)
                .should()
                .findAll();
    }

    @Test
    void givenBookings_whenGetAllBookings_thenReturnAllBookings() {
        var expectedBookings = generateTestBookings();
        var actualBookings = bookingService.getAllBookings();

        BDDAssertions.then(actualBookings)
                .isEqualTo(expectedBookings);
    }

    private static List<Booking> generateTestBookings() {
        var booking1 = Booking.builder()
                .id(1L)
                .property(Property.builder()
                        .id(1L)
                        .name("The Greenhouse Loft")
                        .location("London")
                        .type("Apartment")
                        .build())
                .checkIn(LocalDate.of(2025, Month.MARCH, 18))
                .checkOut(LocalDate.of(2025, Month.MARCH, 21))
                .build();

        var booking2 = Booking.builder()
                .id(2L)
                .property(Property.builder()
                        .id(2L)
                        .name("Riverside House")
                        .location("Birmingham")
                        .type("House")
                        .build())
                .checkIn(LocalDate.of(2025, Month.MARCH, 20))
                .checkOut(LocalDate.of(2025, Month.MARCH, 23))
                .build();

        return List.of(booking1, booking2);
    }
}