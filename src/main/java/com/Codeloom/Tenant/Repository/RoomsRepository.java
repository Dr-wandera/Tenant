package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, Long> {
}
