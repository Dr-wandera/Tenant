package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.LandLord;
import com.Codeloom.Tenant.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LandlordRepository extends JpaRepository<LandLord, Long> {

    Optional<LandLord> findByUser(Users user);
}
