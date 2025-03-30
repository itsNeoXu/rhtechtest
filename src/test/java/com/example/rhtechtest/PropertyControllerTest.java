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
@WebMvcTest(PropertyController.class)
public class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        var testProperties = generateTestProperties();

        given(propertyService.getAllProperties())
                .willReturn(testProperties);
    }

    @Test
    public void whenGetAllProperties_thenCallService() throws Exception {
        mockMvc.perform(get("/properties"));

        then(propertyService)
                .should()
                .getAllProperties();
    }

    @Test
    public void givenProperties_whenGetAllProperties_thenReturnAllProperties() throws Exception {
        var expectedProperties = generateTestProperties();

        given(propertyService.getAllProperties())
                .willReturn(expectedProperties);

        var mvcResult = mockMvc.perform(get("/properties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        var actualProperties = mapper.readValue(
                mvcResult.getResponse().getContentAsString(), new TypeReference<List<Property>>() {
                });

        assertThat(actualProperties, containsInAnyOrder(expectedProperties.toArray()));
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

        return List.of(property1, property2);
    }
}