package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.BookingRequest;
import com.Codeloom.Tenant.Enum.BookingStatus;
import com.Codeloom.Tenant.Enum.Status;
import com.Codeloom.Tenant.Models.*;
import com.Codeloom.Tenant.Repository.BookingRepository;
import com.Codeloom.Tenant.Repository.ClientRepository;
import com.Codeloom.Tenant.Repository.HostelRepository;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Service.Interface.BookingServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingServiceInterface {

    private final BookingRepository bookingRepository;
    private final UserServiceImplementation userServiceImplementation;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final HostelRepository hostelRepository;

    @Override
    public void bookingHostel(BookingRequest bookingRequest) {

        //look for logged_in user
        Users users =userServiceImplementation.getLoggedInUser();

        String email=users.getEmail();

        // Find the user
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find the client profile belonging to that user
        Clients client = clientRepository.findByUser(user).get();

        Hostels hostel=hostelRepository.findByHostelNumber(bookingRequest.getHostelNumber())
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        //todo check number of available rooms before saving it

        Bookings bookings
                = Bookings.builder()
                .createdAt(LocalDateTime.now())
                .status(BookingStatus.PENDING)
                .client(client)
                .updatedAt(LocalDateTime.now())
                .hostel(hostel)
                .checkIn(LocalDate.now())
                .nextInvoiceDate(LocalDate.now().plusDays(15))
                .checkOut(LocalDate.now().plusDays(45))
                .build();
        bookingRepository.save(bookings);

        //change the number of total room after every book
    }
}
