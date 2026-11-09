package com.Codeloom.Tenant.Controller;


import com.Codeloom.Tenant.DTO.LandLordRequest;
import com.Codeloom.Tenant.DTO.LandLordResponse;
import com.Codeloom.Tenant.Service.Implem.LandLordServiceImplementation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LandlordController {

    private final LandLordServiceImplementation landLordServiceImplementation;

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAnyRole('LANDLORD')")
    public LandLordResponse updateLandlord(@RequestBody LandLordRequest landLordRequest) {
        return landLordServiceImplementation.updateLandlord(landLordRequest);
    }
}
