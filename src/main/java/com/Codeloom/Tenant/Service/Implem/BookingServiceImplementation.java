package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.Enum.BookingStatus;
import com.Codeloom.Tenant.Enum.RoomStatus;
import com.Codeloom.Tenant.Models.*;
import com.Codeloom.Tenant.Repository.*;
import com.Codeloom.Tenant.Service.Interface.BookingServiceInterface;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingServiceInterface {

    private final BookingRepository bookingRepository;
    private final UserServiceImplementation userServiceImplementation;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoomsRepository  roomsRepository;
    private final NotificationServiceImplementation notificationServiceImplementation;

    @Transactional
    @Override
    public void bookingRoom(String roomId) throws MessagingException, IOException {

        //look for logged_in user
        Users users =userServiceImplementation.getLoggedInUser();

        String email=users.getEmail();

        // Find the user
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find the client profile belonging to that user
        Clients client = clientRepository.findByUser(user).get();

        Rooms rooms=roomsRepository.findByRoomId(roomId)
                .orElseThrow(()->new RuntimeException("Room not found"));

        //check the booking status before saving the booking if pending meaning room already booked
        if (bookingRepository.findByStatus(BookingStatus.PENDING).isPresent()){
            throw new RuntimeException("Room is already booked");
        }

        Bookings bookings
                = Bookings.builder()
                .createdAt(LocalDateTime.now())
                .status(BookingStatus.PENDING)
                .client(client)
                .updatedAt(LocalDateTime.now())
                .room(rooms)
                .checkIn(LocalDate.now())
                .nextInvoiceDate(LocalDate.now().plusDays(15))
                .checkOut(LocalDate.now().plusDays(45))
                .build();
        bookingRepository.save(bookings);

        //change room status to occupied after room booked
        rooms.setStatus(RoomStatus.PROCESSING);
        roomsRepository.save(rooms);

        //sends notification to landlord/ caretaker after client make a room book
        //room connect us to hostel it belong, and the hostel managers
        notificationServiceImplementation.emailNotificationToLandlordAndCaretaker(
                rooms.getHostel().getLandLord().getUser(),
                rooms.getHostel().getCaretakers().getUser(),
                client.getUser().getPhoneNumber(), //the logged in client that making booking
                client.getUser().getFirstName(),
                rooms.getRoomNumber(),
                rooms.getHostel().getHostelName()
        );

        //sends notification to client of the book made
        notificationServiceImplementation.emailNotificationToClient(
                client.getUser().getEmail(),
                rooms.getHostel().getHostelName(),
                rooms.getHostel().getLandLord().getFirstName(),
                rooms.getHostel().getLandLord().getPhoneNumber(),
                rooms.getHostel().getCaretakers().getFirstName(),
                rooms.getHostel().getCaretakers().getPhoneNumber(),
                rooms.getRoomNumber()

        );
        
    }
    //Approves room booked by user after fulfilling the requirement
    @Transactional
    @Override
    public void approvesPendingRoomBooked(String roomId) {

        Rooms room =roomsRepository.findByRoomId(roomId)
                .orElseThrow(()->new RuntimeException("Room not found"));

        // 2. Fetch the PENDING booking for this specific room
        Bookings booking = bookingRepository.findByStatus( BookingStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No pending booking found for this room"));

        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        room.setStatus(RoomStatus.OCCUPIED);
        roomsRepository.save(room);

    }

    //client cancels booked room
    @Override
    public void cancelBooking(String roomId) {
        Rooms rooms =roomsRepository.findByRoomId(roomId)
                .orElseThrow(()->new RuntimeException("Room not found"));

        // 2. Fetch the PENDING booking for this specific room
        Bookings booking = bookingRepository.findByStatus( BookingStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No pending booking found for this room"));

        rooms.setStatus(RoomStatus.AVAILABLE);
        roomsRepository.save(rooms);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

    }
}
