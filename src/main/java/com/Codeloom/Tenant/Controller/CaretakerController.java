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
    public CaretakerResponse registerCaretaker(@RequestBody CaretakerRequest caretakerRequest) throws MessagingException, IOException {
        return caretakerServiceImplementation.registerCaretaker(caretakerRequest);
    }

    @GetMapping("/{idNumber}")
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CaretakerResponse getCaretakerByIdNumber(@PathVariable String idNumber){
        return caretakerServiceImplementation.getCaretakerByIdNumber(idNumber);
    }
    @PutMapping("/update/{caretakerId}")
    @PreAuthorize("hasAnyRole('LANDLORD','ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CaretakerResponse updateCaretaker(@PathVariable Long caretakerId,@RequestBody CaretakerRequest caretakerRequest){
       return caretakerServiceImplementation.updateCaretaker(caretakerId, caretakerRequest);
    }

    //todo caretaker per logged in landlord hostel
}
