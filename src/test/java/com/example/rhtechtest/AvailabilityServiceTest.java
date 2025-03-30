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
public class AvailabilityServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        var testBookings = generateTestBookings();

        given(bookingRepository.findAll())
                .willReturn(testBookings);

        var testProperties = generateTestProperties();

        given(propertyRepository.findAll())
                .willReturn(testProperties);
    }

    @Test
    void whenGetAllAvailability_thenCallBookingRepository() {
        var start = LocalDate.of(2025, Month.MARCH, 1);
        var end = LocalDate.of(2025, Month.MARCH, 31);

        availabilityService.getAllAvailability(start, end);

        BDDMockito.then(bookingRepository)
                .should()
                .findAll();
    }

    @Test
    void whenGetAllAvailability_thenCallPropertyRepository() {
        var start = LocalDate.of(2025, Month.MARCH, 1);
        var end = LocalDate.of(2025, Month.MARCH, 31);

        availabilityService.getAllAvailability(start, end);

        BDDMockito.then(propertyRepository)
                .should()
                .findAll();
    }

    @Test
    void whenGetAllAvailability_thenReturnAllAvailability() {
        var expectedAvailabilities = generateTestAvailabilities();

        var start = LocalDate.of(2025, Month.MARCH, 1);
        var end = LocalDate.of(2025, Month.MARCH, 31);

        var actualAvailabilities = availabilityService.getAllAvailability(start, end);

        BDDAssertions.then(actualAvailabilities)
                .isEqualTo(expectedAvailabilities);
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
                        .id(3L)
                        .name("Riverside House")
                        .location("Birmingham")
                        .type("House")
                        .build())
                .checkIn(LocalDate.of(2025, Month.MARCH, 20))
                .checkOut(LocalDate.of(2025, Month.MARCH, 23))
                .build();

        return List.of(booking1, booking2);
    }

    private static List<Property> generateTestProperties() {
        var property1 = Property.builder()
                .id(1L)
                .name("The Greenhouse Loft")
                .location("London")
                .type("Apartment")
                .build();

        var property2 = Property.builder()
                .id(2L)
                .name("CityView Studios")
                .location("Manchester")
                .type("Studio")
                .build();

        var property3 = Property.builder()
                .id(3L)
                .name("Riverside House")
                .location("Birmingham")
                .type("House")
                .build();

        var property4 = Property.builder()
                .id(4L)
                .name("The Docklands Retreat")
                .location("London")
                .type("Apartment")
                .build();

        var property5 = Property.builder()
                .id(5L)
                .name("York Street Flats")
                .location("Leeds")
                .type("Apartment")
                .build();

        return List.of(property1, property2, property3, property4, property5);
    }

    private List<Availability> generateTestAvailabilities() {
        var availability1 = Availability.builder()
                .propertyId(1L)
                .status(Availability.Status.NOT_AVAILABLE)
                .build();

        var availability2 = Availability.builder()
                .propertyId(2L)
                .status(Availability.Status.AVAILABLE)
                .build();

        var availability3 = Availability.builder()
                .propertyId(3L)
                .status(Availability.Status.NOT_AVAILABLE)
                .build();

        var availability4 = Availability.builder()
                .propertyId(4L)
                .status(Availability.Status.AVAILABLE)
                .build();

        var availability5 = Availability.builder()
                .propertyId(5L)
                .status(Availability.Status.AVAILABLE)
                .build();

        return List.of(availability1, availability2, availability3, availability4, availability5);
    }
}