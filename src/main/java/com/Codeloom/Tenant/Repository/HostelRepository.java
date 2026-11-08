package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Hostels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelRepository extends JpaRepository<Hostels, Long> {
    Optional<Hostels> findByHostelNameIgnoreCase(String hostelName);

    Optional<Hostels> findByHostelNameContainingIgnoreCase(String hostelName);

    Optional<Hostels> findByHostelId(Long hostelId);

    @Query("SELECT h FROM Hostels h WHERE h.landLord.landLordId = :landlordId")
    Page<Hostels> findHostelsByLandLordId(@Param("landlordId") Long landlordId, Pageable pageable);

}
