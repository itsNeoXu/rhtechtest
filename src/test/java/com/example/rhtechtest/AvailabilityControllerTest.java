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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AvailabilityController.class)
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        var testAvailabilities = generateTestAvailabilities();

        given(availabilityService.getAllAvailability(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(testAvailabilities);
    }

    @Test
    public void givenRequestParams_whenGetAllAvailabilities_thenCallServiceWithParams() throws Exception {
        var expectedStart = LocalDate.of(2025, Month.MARCH, 1);
        var expectedEnd = LocalDate.of(2025, Month.MARCH, 31);

        mockMvc.perform(get("/availability?start=2025-03-01&end=2025-03-31"));

        then(availabilityService)
                .should()
                .getAllAvailability(expectedStart, expectedEnd);
    }

    @Test
    public void givenAvailabilities_whenGetAllAvailabilities_thenReturnAllAvailabilities() throws Exception {
        var expectedStart = LocalDate.of(2025, Month.MARCH, 1);
        var expectedEnd = LocalDate.of(2025, Month.MARCH, 31);
        var expectedAvailabilities = generateTestAvailabilities();

        given(availabilityService.getAllAvailability(expectedStart, expectedEnd))
                .willReturn(expectedAvailabilities);

        var mvcResult = mockMvc.perform(get("/availability?start=2025-03-01&end=2025-03-31")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andReturn();

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var actualAvailabilities = mapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Availability>>() {
                });

        assertThat(actualAvailabilities, containsInAnyOrder(expectedAvailabilities.toArray()));
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