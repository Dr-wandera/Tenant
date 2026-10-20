package com.Codeloom.Tenant.Models;

import jakarta.persistence.*;
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
public class LandLord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long landLordId;
    private String landLordAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @OneToMany(
            mappedBy = "landLord",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Hostels> hostels = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;
}
