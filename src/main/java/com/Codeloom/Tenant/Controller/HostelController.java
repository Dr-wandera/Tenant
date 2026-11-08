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
    public HostelResponse AddHostel(@RequestBody HostelRequest hostelRequest){
        return hostelServiceImplementation.createHostel(hostelRequest);

    }
    @PutMapping("/update/{hostelId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER','ADMIN')")
    public HostelResponse updateHostel(@PathVariable Long hostelId,@RequestBody HostelRequest hostelRequest){
         return hostelServiceImplementation.updateHostel(hostelId, hostelRequest);
    }
    @DeleteMapping("/delete/{hostelId}")
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteHostel(@PathVariable Long hostelId){
        hostelServiceImplementation.deleteHostel(hostelId);
        return "Hostel Deleted successfully";
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD')")
    public List<HostelResponse> allHostelPerLandLord(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return hostelServiceImplementation.allHostelPerLandLord(page,size);
    }

    @GetMapping("/tenants")
    @PreAuthorize("hasAnyRole('LANDLORD')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllTenantPerHostel(
            @RequestParam Long hostelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return hostelServiceImplementation.getAllTenantPerHostel(hostelId, page, size);
    }
}

