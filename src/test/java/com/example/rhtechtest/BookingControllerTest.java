package com.example.rhtechtest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        var testBookings = generateTestBookings();

        given(bookingService.getAllBookings())
                .willReturn(testBookings);
    }

    @Test
    public void whenGetAllBookings_thenCallService() throws Exception {
        mockMvc.perform(get("/bookings"));

        then(bookingService)
                .should()
                .getAllBookings();
    }

    @Test
    public void givenBookings_whenGetAllBookings_thenReturnAllBookings() throws Exception {
        var expectedBookings = generateTestBookings();

        given(bookingService.getAllBookings())
                .willReturn(expectedBookings);

        var mvcResult = mockMvc.perform(get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var actualBookings = mapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Booking>>() {
                });

        assertThat(actualBookings, containsInAnyOrder(expectedBookings.toArray()));
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