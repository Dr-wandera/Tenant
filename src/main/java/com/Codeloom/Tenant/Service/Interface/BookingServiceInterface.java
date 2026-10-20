package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.BookingRequest;
import com.Codeloom.Tenant.Models.Bookings;

public interface BookingServiceInterface {
    void bookingHostel(BookingRequest bookingsRequest);
}
