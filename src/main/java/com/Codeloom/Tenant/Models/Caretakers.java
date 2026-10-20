package com.Codeloom.Tenant.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "caretakers")
public class Caretakers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caretakerId;

    @Column(nullable = false)
    private LocalDateTime employmentStartDate;

    private LocalDateTime employmentEndDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private Users user;

    @OneToMany(mappedBy = "caretakers", fetch = FetchType.LAZY)
    private List<Hostels> hostels = new ArrayList<>();
}
