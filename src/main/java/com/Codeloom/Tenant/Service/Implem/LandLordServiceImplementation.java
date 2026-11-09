package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.LandLordRequest;
import com.Codeloom.Tenant.DTO.LandLordResponse;
import com.Codeloom.Tenant.Models.LandLord;
import com.Codeloom.Tenant.Models.Users;
import com.Codeloom.Tenant.Repository.LandlordRepository;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Service.Interface.LandLordServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LandLordServiceImplementation implements LandLordServiceInterface {
    private final LandlordRepository landlordRepository;
    private final UserServiceImplementation userServiceImplementation;
    private final UserRepository userRepository;

    @Override
    public LandLordResponse updateLandlord(LandLordRequest landLordRequest) {

        Users users= userServiceImplementation.getLoggedInUser();

        Long landLordId = users.getUserId();

        LandLord landLord = landlordRepository.findByUser_UserId(landLordId);

        if(Objects.nonNull(landLordRequest.getFirstName()) && !"".equalsIgnoreCase(landLordRequest.getFirstName())){
            landLord.setFirstName(landLordRequest.getFirstName());
        }
        if ((Objects.nonNull(landLordRequest.getLastName()) && !"".equalsIgnoreCase(landLordRequest.getLastName()))){
            landLord.setLastName(landLordRequest.getLastName());
        }
        if(Objects.nonNull(landLordRequest.getPhoneNumber()) && !"".equalsIgnoreCase(landLordRequest.getPhoneNumber())){
            landLord.setPhoneNumber(landLordRequest.getPhoneNumber());
        }
        if (Objects.nonNull(landLordRequest.getLandLordAddress() ) && !"".equalsIgnoreCase(landLordRequest.getLandLordAddress())){
            landLord.setLandLordAddress(landLordRequest.getLandLordAddress());
        }

        var savedUpdate=landlordRepository.save(landLord);
        userRepository.save(landLord.getUser());  //updates user details related to landlord in user database

        return toResponse(savedUpdate);
    }

    //convert landlord entity to response
    private LandLordResponse toResponse(LandLord savedUpdate) {
       return LandLordResponse.builder()
                .firstName(savedUpdate.getFirstName())
                .lastName(savedUpdate.getLastName())
                .phoneNumber(savedUpdate.getPhoneNumber())
                .landLordAddress(savedUpdate.getLandLordAddress())
                .build();
    }
}
