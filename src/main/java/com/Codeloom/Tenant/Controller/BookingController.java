package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.BookingRequest;
import com.Codeloom.Tenant.Models.Bookings;
import com.Codeloom.Tenant.Service.Implem.BookingServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImplementation bookingServiceImplementation;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String bookHostel(@RequestBody BookingRequest BookingRequest) {
        bookingServiceImplementation.bookingHostel(BookingRequest);
        return "Your booking is successful, Check your email for Hostel management details";
    }
}
