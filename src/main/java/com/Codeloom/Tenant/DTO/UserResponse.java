package com.Codeloom.Tenant.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;
    private  String phoneNumber;
    private String idNumber;
}

