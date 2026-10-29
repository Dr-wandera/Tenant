package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.CaretakerRequest;
import com.Codeloom.Tenant.DTO.CaretakerResponse;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface CaretakerServiceInterface {
    void registerCaretaker(CaretakerRequest caretakerRequest) throws MessagingException, IOException;

    CaretakerResponse getCaretakerByIdNumber(String idNumber);

    void updateCaretaker(Long caretakerId, CaretakerRequest caretakerRequest);
}
