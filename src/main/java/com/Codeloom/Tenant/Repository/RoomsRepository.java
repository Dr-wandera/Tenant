package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    Optional<Rooms> findByRoomId(String roomId);

    Optional<Rooms> findByRoomNumber(String roomNumber);

    @Query("""
        SELECT r  FROM Rooms r LEFT JOIN FETCH r.client c LEFT JOIN FETCH c.user WHERE r.roomNumber = :roomNumber""")
    Optional<Rooms> findByRoomNumberWithClients(@Param("roomNumber") String roomNumber);}
