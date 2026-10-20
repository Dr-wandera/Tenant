package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Hostels;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelRepository extends JpaRepository<Hostels, Long> {
    Optional<Hostels> findByHostelNameIgnoreCase(String hostelName);

    Optional<Hostels> findByHostelNameContainingIgnoreCase(String hostelName);

    Optional<Hostels> findByHostelNumber(@NotNull(message = "Hostel ID cannot be empty") String hostelNumber);
}
