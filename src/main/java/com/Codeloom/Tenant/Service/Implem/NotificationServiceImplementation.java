package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.EmailConfiguration.EmailServiceImplementation;
import com.Codeloom.Tenant.Models.Users;
import com.Codeloom.Tenant.Service.Interface.NotificationServiceInterface;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationServiceInterface {

    private final EmailServiceImplementation emailServiceImplementation;


    @Override
    public void emailNotificationToLandlordAndCaretaker(
            Users landlord,
            Users caretaker,
            String phoneNumber,
            String firstName,
            String roomNumber,
            String hostelName) throws MessagingException, IOException {

        Map<String, String> variables = new HashMap<>();
        variables.put("RoomNumber", roomNumber);
        variables.put("hostelName", hostelName );
        variables.put("firstName", firstName);
        variables.put("phoneNumber", phoneNumber);

        emailServiceImplementation.notificationToLandlordAndCaretaker(
                landlord.getEmail(),
                caretaker.getEmail(),
                "Room Booked",
                "Booked_room.html",
                variables
        );
    }

    @Override
    public void emailNotificationToClient(
            String clientEmail, String hostelName,
            String landLordFirstName, String landLordPhoneNumber,
            String caretakerFirstName, String caretakerPhoneNumber,
            String roomNumber) throws MessagingException, IOException
    {
        Map<String, String> variables = new HashMap<>();
        variables.put("RoomNumber", roomNumber);
        variables.put("hostelName", hostelName);
        variables.put("caretakerFirstName", caretakerFirstName);
        variables.put("caretakerPhoneNumber", caretakerPhoneNumber);
        variables.put("landlordFirstName", landLordFirstName);
        variables.put("landlordPhoneNumber", landLordPhoneNumber);

        emailServiceImplementation.notificationToClient(
                clientEmail,
                "Booked room",
                "client_notification_room.html",
                variables
        );

    }
}
