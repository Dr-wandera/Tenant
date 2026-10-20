package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.LoginRequest;
import com.Codeloom.Tenant.DTO.UserRequest;
import com.Codeloom.Tenant.Service.Implem.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserServiceImplementation userServiceImplementation;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody UserRequest userRequest) {
               userServiceImplementation.userRegistration(userRequest);
               return "User Registered Successful";
    }

    @PostMapping("/login")
    @Cacheable(value = "LoginResponse", key = "#loginRequest.getEmail()")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        return userServiceImplementation.login(loginRequest);

    }
//    @PostMapping("/verify")
//    public ResponseEntity<?> verify(@RequestBody OtpRequest request) {
//        otpVerificationServiceImplementation.verifyEmailByAOtp(request.getOtpCode());
//        return ResponseEntity.ok("Account verified successfully!");
//    }
}
