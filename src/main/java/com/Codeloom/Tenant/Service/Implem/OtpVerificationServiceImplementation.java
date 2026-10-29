package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.EmailConfiguration.EmailServiceImplementation;
import com.Codeloom.Tenant.Models.OtpVerification;
import com.Codeloom.Tenant.Models.Users;
import com.Codeloom.Tenant.Repository.OtpVerificationRepository;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Service.Interface.OtpVerificationServiceInterface;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpVerificationServiceImplementation implements OtpVerificationServiceInterface {

    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailServiceImplementation emailServiceImplementation;
    private final UserRepository userRepository;

    Map<String, String> saveOtp = new HashMap<>();

    //Generate
    @Override
    public void generateVerificationOtp(Users savedUser) throws MessagingException, IOException {

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        OtpVerification token = new OtpVerification();
        token.setOtpCode(otp);
        token.setUser(savedUser);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        saveOtp.put(savedUser.getEmail(),otp);
        otpVerificationRepository.save(token);

        // Prepare template variables
        Map<String, String> variables = new HashMap<>();
        variables.put("otp", (otp));
        variables.put("username", savedUser.getFirstName() );

        // This sends  OTP email to registered user
        emailServiceImplementation.sendOtpRegisterVerification(
                savedUser.getEmail(),
                "Account Verification OTP",
                "Account_verification.html",
                variables
        );
    }

    //verify  account by passing the otp passed to user during registration
    @Override
    public void accountVerificationOtp(String otpCode) {
        // 1. Find otp if not in the database or expired throw invalid otp
        OtpVerification otp = otpVerificationRepository.findByOtpCode(otpCode)
                .orElseThrow(() -> new RuntimeException("Invalid OTP check and try again"));


        // Checks the  expiration time
        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }


        Users user = otp.getUser();

        // 4. Verify user
        user.setActive(true);

        userRepository.save(user);
        log.info("Account activated successfully for: {}", user.getEmail());

        saveOtp.remove(user.getEmail());
        otpVerificationRepository.delete(otp);
    }

    @Override
    public void resendVerificationOtp(String otpCode) throws MessagingException, IOException {
        OtpVerification otpEntity = otpVerificationRepository.findByOtpCode(otpCode)
                .orElseThrow(() -> new RuntimeException("Invalid OTP. Check and try again"));

        Users user = otpEntity.getUser();

        if (user.isActive()) {
            throw new RuntimeException("Account already verified");
        }

        // delete old otp
        otpVerificationRepository.delete(otpEntity);

        // generate new otp
        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification newOtp = new OtpVerification();
        newOtp.setOtpCode(otp);
        newOtp.setUser(user);
        newOtp.setExpiryTime(LocalDateTime.now().plusMinutes(9));

        saveOtp.put(user.getEmail(),otp);
        otpVerificationRepository.save(newOtp);

        // This sends  OTP email to registered user
        // Prepare template variables
        Map<String, String> variables = new HashMap<>();
        variables.put("username", user.getFirstName() );
        variables.put("otp", (otp));

        emailServiceImplementation.sendOtpRegisterVerification(
                user.getEmail(),
                "Account Verification OTP",
                "Account_verification.html",
                variables
        );

    }
}

