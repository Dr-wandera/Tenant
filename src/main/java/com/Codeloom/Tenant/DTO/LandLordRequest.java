package com.Codeloom.Tenant.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LandLordRequest {
    private String landLordAddress;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
