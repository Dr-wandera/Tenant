package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Clients;
import com.Codeloom.Tenant.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Long> {
    Optional<Clients> findByUser(Users user);
}
