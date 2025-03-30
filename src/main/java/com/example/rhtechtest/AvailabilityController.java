package com.example.rhtechtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/availability")
    public List<Availability> getAllAvailability(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return availabilityService.getAllAvailability(start, end);
    }
}
