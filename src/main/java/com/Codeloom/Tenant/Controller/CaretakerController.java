package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.CaretakerRequest;
import com.Codeloom.Tenant.Service.Implem.CaretakerServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/caretaker")
public class CaretakerController {
    private final CaretakerServiceImplementation caretakerServiceImplementation;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public String registerCaretaker(@RequestBody CaretakerRequest caretakerRequest){
        caretakerServiceImplementation.registerCaretaker(caretakerRequest);
        return caretakerRequest.getFirstName()+" Registered successfully";
    }
}
