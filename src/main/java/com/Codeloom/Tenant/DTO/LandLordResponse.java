package com.Codeloom.Tenant.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LandLordResponse {
    private String landLordAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
