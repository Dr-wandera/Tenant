package com.Codeloom.Tenant.Repository;

import com.Codeloom.Tenant.Models.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByOtpCode(String otpCode);
}
