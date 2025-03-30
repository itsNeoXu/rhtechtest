package com.example.rhtechtest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability {

    enum Status {
        AVAILABLE,
        NOT_AVAILABLE
    }

    private Long propertyId;
    private Status status;
}
