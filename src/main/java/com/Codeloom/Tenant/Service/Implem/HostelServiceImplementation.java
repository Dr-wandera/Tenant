package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.HostelRequest;
import com.Codeloom.Tenant.DTO.HostelResponse;
import com.Codeloom.Tenant.Models.*;
import com.Codeloom.Tenant.Repository.*;
import com.Codeloom.Tenant.Service.Interface.HostelServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HostelServiceImplementation implements HostelServiceInterface {
    private final HostelRepository hostelRepository;
    private final LandlordRepository landlordRepository;
    private final UserRepository userRepository;
    private final CaretakerRepository caretakerRepository;

    @Override
    public void createHostel(HostelRequest hostelRequest) {


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
                .hostelImage(hostelRequest.getHostelImage().getBytes())
                .hostelName(hostelRequest.getHostelName())
                .hostelDescription(hostelRequest.getHostelDescription())
                .address(hostelRequest.getAddress())
                .location(hostelRequest.getLocation())
                .caretakers(caretakers)
                .paymentDetails(hostelRequest.getPaymentDetails())
                .build();

        hostelRepository.save(hostel);

    }

    //Get all hostel of a landlord
    @Override
    public List<HostelResponse> getAllHostelUnderLandlord() {

        return hostelRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    //get hostel by hostel name
    @Override
    public List<HostelResponse> getByHostelName(String hostelName) {
        Hostels hostels =hostelRepository.findByHostelNameContainingIgnoreCase(hostelName)
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        // If list is empty → return all products
        if (hostels==null) {
            log.warn("No product found with name: {}", hostelName);
            return hostelRepository.findAll()
                    .stream()
                    .map(this::toDto)
                    .toList();
        }

        return Collections.singletonList(toDto(hostels));
    }

    @Override
    public List<HostelResponse> allHostel(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hostelRepository.findAll(pageable)
                .stream()
                .map(this::toDto)
                .toList();

    }

    @Override
    public void updateHostel(String hostelId, HostelRequest hostelRequest) {
        Hostels hostels= hostelRepository.findByHostelId(hostelId)
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        if (Objects.nonNull(hostelRequest.getHostelImage())&& !"".equalsIgnoreCase(hostelRequest.getHostelImage())) {
            hostels.setHostelImage(hostelRequest.getHostelImage().getBytes());
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

    }

    @Override
    public void deleteHostel(String hostelId) {
        Hostels hostels =hostelRepository.findByHostelId(hostelId)
                .orElseThrow(()->new RuntimeException("Hostel not found"));

        hostelRepository.delete(hostels);
    }


    //converts entity to dto
    private HostelResponse toDto(Hostels hostels) {
      return   HostelResponse.builder()
                .hostelName(hostels.getHostelName())
                .hostelPhoto(Arrays.toString(hostels.getHostelImage()))
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
