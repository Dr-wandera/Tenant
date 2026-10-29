package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.Models.Users;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface OtpVerificationServiceInterface {
    void generateVerificationOtp(Users savedUser) throws MessagingException, IOException;



    //verify  account by passing the otp passed to user during registration
    void accountVerificationOtp(String otpCode);

    void resendVerificationOtp(String otpCode) throws MessagingException, IOException;
}
