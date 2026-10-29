package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.Service.Implem.BookingServiceImplementation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingServiceImplementation bookingServiceImplementation;

    @PostMapping("/room/{roomId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String bookHostelRoom(@PathVariable String roomId) throws MessagingException, IOException {
        bookingServiceImplementation.bookingRoom(roomId);
        return "Your booking is successful, Check your email for Hostel management details";
    }

    @PatchMapping("/approve/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER')")
    public String approvesPendingRoomBooked(@PathVariable String roomId){
        bookingServiceImplementation.approvesPendingRoomBooked(roomId);
        return "Room approved successfully";
    }

    @PatchMapping("/cancel/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    public String cancelBooking(@PathVariable String roomId){
        bookingServiceImplementation.cancelBooking(roomId);
        return "Room cancelled successfully";
    }

}
