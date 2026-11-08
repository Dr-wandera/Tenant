package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Enum.BookingStatus;
import com.Codeloom.Tenant.Models.Bookings;
import com.Codeloom.Tenant.Models.Rooms;
import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Bookings, Long> {
    Optional<Bookings> findByStatus(BookingStatus bookingStatus);


    Optional<Rooms> findByRoomAndStatus(com.Codeloom.Tenant.Models.Rooms rooms, BookingStatus bookingStatus);
}
