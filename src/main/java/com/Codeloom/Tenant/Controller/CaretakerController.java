package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.CaretakerRequest;
import com.Codeloom.Tenant.DTO.CaretakerResponse;
import com.Codeloom.Tenant.Service.Implem.CaretakerServiceImplementation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/caretaker")
public class CaretakerController {
    private final CaretakerServiceImplementation caretakerServiceImplementation;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'LANDLORD')")
    public String registerCaretaker(@RequestBody CaretakerRequest caretakerRequest) throws MessagingException, IOException {
        caretakerServiceImplementation.registerCaretaker(caretakerRequest);
        return caretakerRequest.getFirstName()+" Registered successfully";
    }

    @GetMapping("/{idNumber}")
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    public CaretakerResponse getCaretakerByIdNumber(@PathVariable String idNumber){
        return caretakerServiceImplementation.getCaretakerByIdNumber(idNumber);
    }
    @PutMapping("/update/{caretakerId}")
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    public String updateCaretaker(@PathVariable Long caretakerId,@RequestBody CaretakerRequest caretakerRequest){
        caretakerServiceImplementation.updateCaretaker(caretakerId, caretakerRequest);
        return "Caretaker Updated successfully";
    }

    //todo caretaker per logged in landlord hostel
}
