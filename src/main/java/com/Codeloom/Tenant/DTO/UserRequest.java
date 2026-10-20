package com.Codeloom.Tenant.DTO;

import com.Codeloom.Tenant.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private  String phoneNumber;
    private String idNumber;

}
