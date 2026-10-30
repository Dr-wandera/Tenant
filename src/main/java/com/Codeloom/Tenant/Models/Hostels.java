package com.Codeloom.Tenant.Models;

import com.Codeloom.Tenant.Enum.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "hostels")
public class Hostels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostelId;
    @Column(length = 30, unique = true)
    private String hostelNumber;

    @NotBlank(message = "Hostel name required")
    @Column(nullable = false, length = 150)
    private String hostelName;

    @NotBlank(message = "Hostel photo required")
    @Column(nullable = false)
    private String hostelImage;


    @NotBlank(message = "Address required")
    @Column(nullable = false, length = 255)
    private String address;

    @NotBlank(message = "Hostel description required")
    @Column(nullable = false, length = 2000)
    private String hostelDescription;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Location location;

    @NotBlank(message = "Payment details required")
    @Column(name = "payment_details", nullable = false, length = 500)
    private String paymentDetails;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "landlord_id", nullable = false)
    private LandLord landLord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caretaker_id")
    private Caretakers caretakers;

    @OneToMany(mappedBy = "hostel", fetch = FetchType.LAZY)
    private List<Rooms> rooms = new ArrayList<>();

}
