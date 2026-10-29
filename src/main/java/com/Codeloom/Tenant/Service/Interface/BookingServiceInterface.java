package com.Codeloom.Tenant.Service.Interface;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface BookingServiceInterface {
    void bookingRoom(String roomId) throws MessagingException, IOException;

    void approvesPendingRoomBooked(String roomId);

    void cancelBooking(String roomId);
}
