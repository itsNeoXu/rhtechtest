package com.example.rhtechtest;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        var expectedProperties = generateTestProperties();

        given(propertyRepository.findAll())
                .willReturn(expectedProperties);
    }

    @Test
    void whenGetAllProperties_thenCallRepository() {
        propertyService.getAllProperties();

        BDDMockito.then(propertyRepository)
                .should()
                .findAll();
    }

    @Test
    void givenProperties_whenGetAllProperties_thenReturnProperties() {
        var expectedProperties = generateTestProperties();
        var actualProperties = propertyService.getAllProperties();

        BDDAssertions.then(actualProperties)
                .as("Description")
                .isEqualTo(expectedProperties);
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