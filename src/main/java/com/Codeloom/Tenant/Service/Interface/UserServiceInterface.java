package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.*;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface UserServiceInterface {
    UserResponse userRegistration(UserRequest userRequest) throws MessagingException, IOException;

    ResponseEntity<?> login(LoginRequest loginRequest);

    void verifyAccountOtp(String otpCode);

    void resendOtp(String otpCode) throws MessagingException, IOException;

    Number totalUsers();

    void createPasswordResetToken(ForgetPasswordRequest forgetPasswordRequest) throws MessagingException, IOException;

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
