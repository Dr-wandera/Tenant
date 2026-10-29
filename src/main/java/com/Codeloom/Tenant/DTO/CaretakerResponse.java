package com.Codeloom.Tenant.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaretakerResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String idNumber;
}
