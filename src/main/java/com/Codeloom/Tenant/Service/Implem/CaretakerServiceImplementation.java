package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.CaretakerRequest;
import com.Codeloom.Tenant.Enum.Role;
import com.Codeloom.Tenant.Models.Caretakers;
import com.Codeloom.Tenant.Models.Users;
import com.Codeloom.Tenant.Repository.CaretakerRepository;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Service.Interface.CaretakerServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CaretakerServiceImplementation implements CaretakerServiceInterface {
    private final UserRepository userRepository;
    private final CaretakerRepository caretakerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void registerCaretaker(CaretakerRequest caretakerRequest) {

        //checks if there is any user with the email already in the database before saving another user with the email
        if (userRepository.findByEmail(caretakerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Users users=toEntity(caretakerRequest);

        userRepository.save(users);

        Caretakers caretakers =Caretakers.builder()
                .user(users)
                .employmentStartDate(LocalDateTime.now())
                .build();

        caretakerRepository.save(caretakers);


    }

    private Users toEntity(CaretakerRequest caretakerRequest) {
       return Users.builder()
                .email(caretakerRequest.getEmail())
                .password(passwordEncoder.encode(caretakerRequest.getPassword()))
                .userNumber(generateCaretakerNumber())
                .active(true)
                .role(Role.CARETAKER)
                .firstName(caretakerRequest.getFirstName())
                .lastName(caretakerRequest.getLastName())
                .phoneNumber(caretakerRequest.getPhoneNumber())
                .idNumber(caretakerRequest.getIdNumber())
                .build();
    }

    //generate caretaker number, unique number
    private String generateCaretakerNumber() {
        SecureRandom random = new SecureRandom();

        // Generates a random number between 100000 and 999999 inclusive
        int number = 100000 + random.nextInt(900000);

        return String.valueOf(number);
    }
}
