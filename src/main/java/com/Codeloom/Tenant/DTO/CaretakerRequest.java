package com.Codeloom.Tenant.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaretakerRequest {

    private String firstName;

    private String lastName;

    @NotBlank(message = "Email required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Phone number required")
    private String phoneNumber;

    @NotBlank(message = "ID number required")
    private String idNumber;

    @NotBlank(message = "Password required")
    private String password;
}