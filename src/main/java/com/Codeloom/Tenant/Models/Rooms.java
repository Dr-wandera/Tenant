package com.Codeloom.Tenant.Models;

import com.Codeloom.Tenant.Enum.RoomStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NotBlank(message = "Room number required")
    @Column(name = "room_number", nullable = false, length = 30)
    private String roomNumber;

    @NotNull(message = "Room price required")
    @DecimalMin(value = "0.00", inclusive = false)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyRent;

    @Column(length = 500)
    private String roomPhoto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hostel_id", nullable = false)
    private Hostels hostel;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Bookings> bookings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients client;
}
