package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.LoginRequest;
import com.Codeloom.Tenant.DTO.LoginResponse;
import com.Codeloom.Tenant.DTO.UserRequest;
import com.Codeloom.Tenant.Enum.ClientType;
import com.Codeloom.Tenant.Enum.Role;
import com.Codeloom.Tenant.Models.Caretakers;
import com.Codeloom.Tenant.Models.Clients;
import com.Codeloom.Tenant.Models.LandLord;
import com.Codeloom.Tenant.Models.Users;
import com.Codeloom.Tenant.Repository.CaretakerRepository;
import com.Codeloom.Tenant.Repository.ClientRepository;
import com.Codeloom.Tenant.Repository.LandlordRepository;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Security.AppUserDetailService;
import com.Codeloom.Tenant.Security.JwtService;
import com.Codeloom.Tenant.Service.Interface.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImplementation implements UserServiceInterface {
    private final UserRepository userRepository;
    private final LandlordRepository landlordRepository;
    private  final ClientRepository clientRepository;
    private final CaretakerRepository caretakerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserDetailService userDetailsService;

    Role assignedRole;

    @Override
    public void userRegistration(UserRequest userRequest) {

        // If role is not provided → automatically assign it to USER
        if (userRequest.getRole() == null) {
            assignedRole = Role.USER;
        }
        // If role is provided → accept it
        else {
            assignedRole = Role.valueOf(userRequest.getRole().name());
        }
        // Check if user already exists
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        else if (userRepository.findByIdNumber(userRequest.getIdNumber()).isPresent()) {
            throw  new RuntimeException("Id number already exists");

        }

        // Convert request to entity
        Users users = toEntity(userRequest);

        // Save the new user
        userRepository.save(users);

        //saves registered user if it is a landlord
        if (userRequest.getRole() == Role.LANDLORD){

            //if user registered is a landlord
            LandLord landLord = LandLord.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .user(users)
                    .phoneNumber(userRequest.getPhoneNumber())
                    .build();
            //save landlord profile
            landlordRepository.save(landLord);
        }
        //Client is user ROLE
        else if (userRequest.getRole() == Role.USER) {
            Clients clients =Clients.builder()
                    .user(users)
                    .clientType(ClientType.STUDENT)
                    .createdAt(LocalDateTime.now())
                    .build();

            clientRepository.save(clients);

        }
        else {
            throw new RuntimeException("Invalid role");
        }


    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Users user=userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new RuntimeException("invalid email! check and try again "));

        /*
              .this check first if user is verified in if not, not allowed to log in
              .this  prevents log in if you have not verified the account
         */

        if (!user.isActive()) {
            throw new RuntimeException("Please verify your account before logging in.");
        }

        log.info("logged in:{}", loginRequest.getEmail());
        try {

            authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            final UserDetails userDetails =
                    userDetailsService.loadUserByUsername(loginRequest.getEmail());

            log.info("User loaded:{}", userDetails.getAuthorities());

            String token = jwtService.generateToken(userDetails);

            log.info("Token created:{}", token, new LoginResponse(loginRequest.getEmail(), token));

            return ResponseEntity.ok()
                    .body(new LoginResponse(loginRequest.getEmail(), token));

        }

        catch (BadCredentialsException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "Incorrect email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( response);

        }
        catch (DisabledException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( response);

        }
        catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", true);
            response.put("message", "Authentication Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body( response);

        }

    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    ///Create Entity object
    private Users toEntity(UserRequest userRequest) {

        return Users.builder()
                .userNumber(generateUserNumber())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .idNumber(userRequest.getIdNumber())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .role(assignedRole)
                .active(true)
                .build();
    }

    //Generate user identification number
    public String generateUserNumber() {
        String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom random = new SecureRandom();
        int length = 10; // Target length for the user number

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    //this get the logged-in user from security context holder
    public Users getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() ||
                Objects.equals(auth.getPrincipal(), "anonymousUser")) {
            return null;
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
