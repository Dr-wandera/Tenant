package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Caretakers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaretakerRepository extends JpaRepository<Caretakers, Long> {
}
