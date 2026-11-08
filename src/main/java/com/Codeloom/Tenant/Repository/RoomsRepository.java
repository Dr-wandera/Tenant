package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.DTO.UserResponse;
import com.Codeloom.Tenant.Enum.RoomStatus;
import com.Codeloom.Tenant.Models.Rooms;
import com.Codeloom.Tenant.Models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {
    Optional<Rooms> findByRoomId(String roomId);

    Optional<Rooms> findByRoomNumber(String roomNumber);

    @Query("""
            SELECT r  FROM Rooms r LEFT JOIN FETCH r.client c LEFT JOIN FETCH c.user WHERE r.roomNumber = :roomNumber""")
    Optional<Rooms> findByRoomNumberWithClients(@Param("roomNumber") String roomNumber);

    @Query("SELECT r FROM Rooms r WHERE r.hostel.hostelId = :hostelId AND r.status = :status")
    Page<Rooms> findRoomsByHostelAndStatus(@Param("hostelId") Long hostelId, @Param("status") RoomStatus status, Pageable pageable
    );

    @Query("""
    SELECT DISTINCT c.user
    FROM Rooms r
    JOIN r.client c
    JOIN c.user u
    WHERE r.hostel.hostelId = :hostelId
""")
    List<Users> findClientsByHostel(
            @Param("hostelId") Long hostelId,
            Pageable pageable
    );
}
