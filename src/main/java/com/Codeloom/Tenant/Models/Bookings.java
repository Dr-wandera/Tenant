package com.Codeloom.Tenant.Models;

import com.Codeloom.Tenant.Enum.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(
        name = "bookings",
        indexes = {
                @Index(
                        name = "idx_booking_client",
                        columnList = "client_id"
                ),
                @Index(
                        name = "idx_booking_room_dates",
                        columnList = "room_id, check_in, check_out"
                ),

        }
)
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Clients client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    @NotNull(message = "Check-in date required")
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @NotNull(message = "Check-out date required")
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    private LocalDate nextInvoiceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @AssertTrue(message = "Check-out must be after check-in")
    public boolean isValidBookingPeriod() {
        return checkIn == null
                || checkOut == null
                || checkOut.isAfter(checkIn);
    }
}
