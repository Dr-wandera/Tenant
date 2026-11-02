package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.CaretakerRequest;
import com.Codeloom.Tenant.DTO.CaretakerResponse;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface CaretakerServiceInterface {
    CaretakerResponse registerCaretaker(CaretakerRequest caretakerRequest) throws MessagingException, IOException;

    CaretakerResponse getCaretakerByIdNumber(String idNumber);

    CaretakerResponse updateCaretaker(Long caretakerId, CaretakerRequest caretakerRequest);
}
