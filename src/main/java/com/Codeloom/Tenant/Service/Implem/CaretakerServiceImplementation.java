package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.CaretakerRequest;
import com.Codeloom.Tenant.DTO.CaretakerResponse;
import com.Codeloom.Tenant.Enum.Role;
import com.Codeloom.Tenant.Models.Caretakers;
import com.Codeloom.Tenant.Models.Users;
import com.Codeloom.Tenant.Repository.CaretakerRepository;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Service.Interface.CaretakerServiceInterface;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CaretakerServiceImplementation implements CaretakerServiceInterface {
    private final UserRepository userRepository;
    private final CaretakerRepository caretakerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImplementation userServiceImplementation;
    private final OtpVerificationServiceImplementation otpVerificationServiceImplementation;

    @Transactional
    @Override
    public void registerCaretaker(CaretakerRequest caretakerRequest) throws MessagingException, IOException {

        //checks if there is any user with the email already in the database before saving another user with the email
        if (userRepository.findByEmail(caretakerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Users users=toEntity(caretakerRequest);

        Users savedUser=userRepository.save(users);

        Caretakers caretakers =Caretakers.builder()
                .user(users)
                .firstName(caretakerRequest.getFirstName())
                .lastName(caretakerRequest.getLastName())
                .employmentStartDate(LocalDateTime.now())
                .employmentEndDate(LocalDateTime.now().plusDays(120))
                .phoneNumber(caretakerRequest.getPhoneNumber())
                .caretakerNumber(generateCaretakerNumber())
                .idNumber(caretakerRequest.getIdNumber())
                .build();

        caretakerRepository.save(caretakers);

        //sends OTP  notification to registered user to verify account
        otpVerificationServiceImplementation.generateVerificationOtp(savedUser);

    }

    @Override
    public CaretakerResponse getCaretakerByIdNumber(String idNumber) {
        Caretakers caretaker =caretakerRepository.findByIdNumber(idNumber)
                .orElseThrow(()-> new RuntimeException("Caretaker not found"));

        return toResponse(caretaker);
    }

    //updates caretaker details
    @Override
    public void updateCaretaker(Long caretakerId, CaretakerRequest caretakerRequest) {
        Caretakers caretakers= caretakerRepository.findByCaretakerId(caretakerId)
                .orElseThrow(()->new RuntimeException("Caretaker not found"));

        if (Objects.nonNull(caretakerRequest.getFirstName())&&!"".equalsIgnoreCase(caretakerRequest.getFirstName())) {
            caretakers.setFirstName(caretakerRequest.getFirstName());
        }
        if (Objects.nonNull(caretakerRequest.getLastName())&&!"".equalsIgnoreCase(caretakerRequest.getLastName())) {
            caretakers.setLastName(caretakerRequest.getLastName());
        }
        if (Objects.nonNull(caretakerRequest.getPhoneNumber())&&!"".equalsIgnoreCase(caretakerRequest.getPhoneNumber())) {
            caretakers.setPhoneNumber(caretakerRequest.getPhoneNumber());
        }
        if (Objects.nonNull(caretakerRequest.getPhoneNumber())&&!"".equalsIgnoreCase(caretakerRequest.getPhoneNumber())) {
            caretakers.setPhoneNumber(caretakerRequest.getPhoneNumber());
        }

        caretakerRepository.save(caretakers);

    }

    //converts caretaker entity to response
    private CaretakerResponse toResponse(Caretakers caretaker) {
       return CaretakerResponse.builder()
                .firstName(caretaker.getFirstName())
                .lastName(caretaker.getLastName())
                .phoneNumber(caretaker.getPhoneNumber())
                .idNumber(caretaker.getIdNumber())
                .build();
    }

    //Generates caretaker identification number
    private String generateCaretakerNumber() {
        return userServiceImplementation.generateUserNumber();
    }

    private Users toEntity(CaretakerRequest caretakerRequest) {
       return Users.builder()
                .email(caretakerRequest.getEmail())
                .password(passwordEncoder.encode(caretakerRequest.getPassword()))
                .userNumber(generateUserNumber())
                .active(true)
                .role(Role.CARETAKER)
                .firstName(caretakerRequest.getFirstName())
                .lastName(caretakerRequest.getLastName())
                .phoneNumber(caretakerRequest.getPhoneNumber())
                .idNumber(caretakerRequest.getIdNumber())
                .build();
    }

    //generate caretaker number, unique number
    private String generateUserNumber() {
        return userServiceImplementation.generateUserNumber();
    }
}
