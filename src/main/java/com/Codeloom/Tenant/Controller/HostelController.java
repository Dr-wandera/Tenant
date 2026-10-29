package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.HostelRequest;
import com.Codeloom.Tenant.DTO.HostelResponse;
import com.Codeloom.Tenant.DTO.UserResponse;
import com.Codeloom.Tenant.Models.Bookings;
import com.Codeloom.Tenant.Service.Implem.HostelServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hostel")
public class HostelController {

    private final HostelServiceImplementation hostelServiceImplementation;

    @GetMapping("/name/{hostelName}")
    @ResponseStatus(HttpStatus.OK)
    public List<HostelResponse> getByHostelName(@PathVariable String hostelName){
        return hostelServiceImplementation.getByHostelName(hostelName);
    }
    @GetMapping("/hostels")
    @ResponseStatus(HttpStatus.OK)
    public List<HostelResponse> allHostel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return hostelServiceImplementation.allHostel(page,size);
    }

    //landlord interface
    @PostMapping("/add_hostel")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    public  String AddHostel(@RequestBody HostelRequest hostelRequest){
        hostelServiceImplementation.createHostel(hostelRequest);
        return "Hostel Added successfully";
    }
    @PutMapping("/update/{hostelId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER','ADMIN')")
    public String updateHostel(@PathVariable String hostelId,@RequestBody HostelRequest hostelRequest){
         hostelServiceImplementation.updateHostel(hostelId, hostelRequest);
         return "Hostel Updated successfully";
    }
    @DeleteMapping("/delete/{hostelId}")
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    public String deleteHostel(@PathVariable String hostelId){
        hostelServiceImplementation.deleteHostel(hostelId);
        return "Hostel Deleted successfully";
    }

//    public UserResponse tenants(){
//        return hostelServiceImplementation.tenants()
//    }

}

