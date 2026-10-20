package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.HostelRequest;
import com.Codeloom.Tenant.DTO.HostelResponse;
import com.Codeloom.Tenant.Repository.UserRepository;
import com.Codeloom.Tenant.Service.Implem.HostelServiceImplementation;
import com.Codeloom.Tenant.Service.Implem.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/landLord")
public class LandlordController {
    private final HostelServiceImplementation hostelServiceImplementation;

    //landlord interface
    @PostMapping("/add_hostel")
    @ResponseStatus(HttpStatus.CREATED)
    public  String AddHostel(@RequestBody HostelRequest hostelRequest){
        hostelServiceImplementation.createHostel(hostelRequest);
        return "Hostel Added successfully";
    }
    //get hostel of the logged_in landlord
    @GetMapping("/hostels")
    public List<HostelResponse> getAllHostelUnderLandlord(){
        return hostelServiceImplementation.getAllHostelUnderLandlord();
    }


}
