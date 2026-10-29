package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.LoginRequest;
import com.Codeloom.Tenant.DTO.OtpRequest;
import com.Codeloom.Tenant.DTO.UserRequest;
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
    public String registerUser(@RequestBody UserRequest userRequest) throws MessagingException, IOException {
               userServiceImplementation.userRegistration(userRequest);
               return "You have Registered Successful. Please verify your email.";
    }

    @PostMapping("/login")
//    @Cacheable(value = "LoginResponse", key = "#loginRequest.getEmail()")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        return userServiceImplementation.login(loginRequest);

    }
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody OtpRequest request) {
        userServiceImplementation.verifyAccountOtp(request.getOtpCode());
        return ResponseEntity.ok("Account verified successfully!");
    }
    @PostMapping("/resendCode")
    public ResponseEntity<?> resendCode(@RequestBody OtpRequest request) throws MessagingException, IOException {
        userServiceImplementation.resendOtp(request.getOtpCode());
        return ResponseEntity.ok("Code sent. Check your email for OTP to activate your account.");
    }
}
