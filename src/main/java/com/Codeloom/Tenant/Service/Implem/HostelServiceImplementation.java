package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.HostelRequest;
import com.Codeloom.Tenant.DTO.HostelResponse;
import com.Codeloom.Tenant.DTO.UserResponse;
import com.Codeloom.Tenant.Models.*;
import com.Codeloom.Tenant.Repository.*;
import com.Codeloom.Tenant.Service.Interface.HostelServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HostelServiceImplementation implements HostelServiceInterface {
    private final HostelRepository hostelRepository;
    private final LandlordRepository landlordRepository;
    private final UserRepository userRepository;
    private final CaretakerRepository caretakerRepository;
    private final UserServiceImplementation userServiceImplementation;
    private final RoomsRepository roomsRepository;

    @CachePut(value = "ADD_HOSTEL_CACHE", key = "#result")
    @Override
    public HostelResponse createHostel(HostelRequest hostelRequest) {


        // Gets logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new UsernameNotFoundException("No logged-in user found");
        }

        String email = auth.getName();
        // Find the user
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find the landlord profile belonging to that user
        LandLord landlord = landlordRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Only Landlord allowed to add hostel!"));

        //checks if there is hostel exist by the name before saving
        if (hostelRepository.findByHostelNameIgnoreCase(hostelRequest.getHostelName()).isPresent()) {
            throw new RuntimeException("Hostel already exists");

        }

        //checks for caretaker profile to be assigned to hostel
        Caretakers caretakers=caretakerRepository.findByIdNumber(hostelRequest.getIdNumber())
                .orElseThrow(()->new RuntimeException("Caretaker not found"));


        //create hostel object
        Hostels hostel = Hostels.builder()
                .landLord(landlord)
                .hostelNumber(generateHostelNumber())
                .hostelImage(hostelRequest.getHostelImage())
                .hostelName(hostelRequest.getHostelName())
                .hostelDescription(hostelRequest.getHostelDescription())
                .address(hostelRequest.getAddress())
                .location(hostelRequest.getLocation())
                .caretakers(caretakers)
                .paymentDetails(hostelRequest.getPaymentDetails())
                .build();

        hostelRepository.save(hostel);
        return toResponse(hostel);

    }

    //Get all hostel of a landlord
    @Cacheable(value = "LANDLORD_HOSTEL_CACHE", key = "'all'")
    @Override
    public List<HostelResponse> getAllHostelUnderLandlord() {

        return hostelRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    //get hostel by hostel name
    @Cacheable(value = "HOSTELNAME_HOSTEL_CACHE", key = "#hostelName")
    @Override
    public List<HostelResponse> getByHostelName(String hostelName) {
        Hostels hostels =hostelRepository.findByHostelNameContainingIgnoreCase(hostelName)
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        // If list is empty → return all products
        if (hostels==null) {
            log.warn("No hostel found with name: {}", hostelName);
            return hostelRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return Collections.singletonList(toResponse(hostels));
    }

    @Cacheable(value = "ALL_HOSTEL_CACHE",key = "'page:' + #page + ':size:' + #size")
    @Override
    public List<HostelResponse> allHostel(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hostelRepository.findAll(pageable)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    @CachePut(value = "UPDATE_HOSTEL_CACHE", key = "#hostelId")
    @Override
    public HostelResponse updateHostel(Long hostelId, HostelRequest hostelRequest) {
        Hostels hostels= hostelRepository.findByHostelId(hostelId)
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        if (Objects.nonNull(hostelRequest.getHostelImage())&& !"".equalsIgnoreCase(hostelRequest.getHostelImage())) {
            hostels.setHostelImage(hostelRequest.getHostelImage());
        }
        if (Objects.nonNull(hostelRequest.getHostelName())&& !"".equalsIgnoreCase(hostelRequest.getHostelName())) {
            hostels.setHostelName(hostelRequest.getHostelName());
        }
        if (Objects.nonNull(hostelRequest.getHostelDescription())&& !"".equalsIgnoreCase(hostelRequest.getHostelDescription())) {
            hostels.setHostelDescription(hostelRequest.getHostelDescription());
        }
        if (Objects.nonNull(hostelRequest.getAddress())&& !"".equalsIgnoreCase(hostelRequest.getAddress())) {
            hostels.setAddress(hostelRequest.getAddress());
        }
        if (Objects.nonNull(hostelRequest.getPaymentDetails())&& !"".equalsIgnoreCase(hostelRequest.getPaymentDetails())) {
            hostels.setPaymentDetails(hostelRequest.getPaymentDetails());
        }
        if(Objects.nonNull(hostelRequest.getPaymentDetails())&&!"".equalsIgnoreCase(hostelRequest.getPaymentDetails())) {
            hostels.setPaymentDetails(hostelRequest.getPaymentDetails());
        }
        if (Objects.nonNull(hostelRequest.getLocation())&&!"".equalsIgnoreCase(String.valueOf(hostelRequest.getLocation()))){
            hostels.setLocation(hostelRequest.getLocation());
        }

        hostelRepository.save(hostels);
        return toResponse(hostels);

    }

    @CacheEvict(value = "DELETE_HOSTEL_CACHE", key = "#hostelId")
    @Override
    public void deleteHostel(Long hostelId) {
        Hostels hostels =hostelRepository.findByHostelId(hostelId)
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        hostelRepository.delete(hostels);
    }

    //gets all hostel of the logged in landlord
    @Cacheable(value = "ALL_HOSTEL_PER_LANDLORD_CACHE",key = "'page:' + #page + ':size:' + #size")
    @Override
    public List<HostelResponse> allHostelPerLandLord( int page, int size) {

        //gets logged in landlord
        Users users = userServiceImplementation.getLoggedInUser();

        //find landlord from landlord table
        LandLord landLord=landlordRepository.findByUser(users).get();

        //gets landlord id to be used for looking hostel that belong to that landlord
        Long landlordId= landLord.getLandLordId();

        Pageable pageable = PageRequest.of(page, size);

        // 3. Query the database using the verified landlord id
        return hostelRepository.findHostelsByLandLordId(landlordId, pageable)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toCollection(ArrayList::new));

    }
    //return tenants a hostel have
    @Cacheable(value = "ALL_TENANT_PER_HOSTEL_CACHE",key = "'page:' + #page + ':size:' + #size")
    @Override
    public List<UserResponse> getAllTenantPerHostel(Long hostelId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        List<Users> users = roomsRepository.findClientsByHostel(hostelId, pageable);

        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    //converts User Entity to response
    private UserResponse toUserResponse(Users users) {
        return UserResponse.builder()
                .idNumber(users.getIdNumber())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .email(users.getEmail())
                .phoneNumber(users.getPhoneNumber())
                .build();
    }

    //converts entity to dto
    private HostelResponse toResponse(Hostels hostels) {
      return   HostelResponse.builder()
                .hostelName(hostels.getHostelName())
                .hostelPhoto(hostels.getHostelImage())
                .address(hostels.getAddress())
                .hostelDescription(hostels.getHostelDescription())
                .location(hostels.getLocation())
                .build();
    }

    //generate hostel identification number
    private String generateHostelNumber() {
        return "HST-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

}
