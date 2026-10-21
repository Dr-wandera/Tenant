package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.Caretakers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaretakerRepository extends JpaRepository<Caretakers, Long> {
    Optional<Caretakers> findByCaretakerNumber(String caretakerNumber);
}
