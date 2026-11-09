package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.*;
import com.Codeloom.Tenant.Service.Implem.UserServiceImplementation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody UserRequest userRequest) throws MessagingException, IOException {
        return userServiceImplementation.userRegistration(userRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        return userServiceImplementation.login(loginRequest);

    }
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> forgotPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) throws MessagingException, IOException {
        userServiceImplementation.createPasswordResetToken(forgetPasswordRequest);
        return ResponseEntity.ok("OTP sent to your email ");
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        userServiceImplementation.resetPassword(resetPasswordRequest);

        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> verify(@RequestBody OtpRequest request) {
        userServiceImplementation.verifyAccountOtp(request.getOtpCode());
        return ResponseEntity.ok("Account verified successfully!");
    }
    @PostMapping("/resendCode")
    public ResponseEntity<?> resendCode(@RequestBody OtpRequest request) throws MessagingException, IOException {
        userServiceImplementation.resendOtp(request.getOtpCode());
        return ResponseEntity.ok("Code sent. Check your email for OTP to activate your account.");
    }

    @GetMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    public Number totalUsers(){
        return userServiceImplementation.totalUsers();
    }
}
