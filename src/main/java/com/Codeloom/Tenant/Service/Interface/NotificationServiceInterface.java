package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.Models.Users;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.IOException;

public interface NotificationServiceInterface {
    void emailNotificationToLandlordAndCaretaker(
            Users user,
            Users user1,
            String phoneNumber,
            String firstName,
            String roomNumber,
            String hostelName) throws MessagingException, IOException;

    void emailNotificationToClient(
            String clientEmail, String hostelName,
            String landLordFirstName, String landLordPhoneNumber,
            String caretakerFirstName,String caretakerPhoneNumber,
            String roomNumber
    ) throws MessagingException, IOException;

    void sendNotificationToUsers(String token, String email, String firstName) throws MessagingException, IOException;

    void notifyLandlordAndCaretaker(@NotBlank(message = "Email required") @Email String email, @NotBlank(message = "Email required") @Email String email1, @NotBlank(message = "Hostel name required") String hostelName, String hostelNumber) throws MessagingException, IOException;
}
