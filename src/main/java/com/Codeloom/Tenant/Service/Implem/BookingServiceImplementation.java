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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation implements BookingServiceInterface {

    private final BookingRepository bookingRepository;
    private final UserServiceImplementation userServiceImplementation;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RoomsRepository  roomsRepository;
    private final NotificationServiceImplementation notificationServiceImplementation;

    private final ConcurrentHashMap<String, ReentrantLock> roomLocks = new ConcurrentHashMap<>();

    @Transactional
    @Override
    public void bookingRoom(String roomId) throws MessagingException, IOException {

        //look for logged_in user
        Users users = userServiceImplementation.getLoggedInUser();

        String email = users.getEmail();

        // Find the user
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find the client profile belonging to that user
        Clients client = clientRepository.findByUser(user).get();

        //create a unique lock for this specific roomId
        ReentrantLock lock = roomLocks.computeIfAbsent(roomId, id -> new ReentrantLock());

        // Variable initialization to make 'rooms' accessible outside the try-catch block for notifications
        Rooms rooms;

        // Acquire the lock. Concurrent threads for the SAME room will wait here.
        lock.lock();
        try {
            rooms = roomsRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            //  Check if THIS SPECIFIC ROOM already has a pending or processing status
            if (bookingRepository.findByRoomAndStatus(rooms, BookingStatus.PENDING).isPresent()
                    || rooms.getStatus() == RoomStatus.PROCESSING) {
                throw new RuntimeException("Room is already booked or processing");
            }

            // Create the booking
            Bookings bookings = Bookings.builder()
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

            // Change room status after room booked
            rooms.setStatus(RoomStatus.PROCESSING);
            rooms.setClient(client);
            roomsRepository.save(rooms);

        } finally {
            // 4. ALWAYS unlock in a finally block to prevent permanent deadlocks on failure
            lock.unlock();
        }



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
