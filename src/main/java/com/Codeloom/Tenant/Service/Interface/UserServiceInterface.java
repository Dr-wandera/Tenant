package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.LoginRequest;
import com.Codeloom.Tenant.DTO.UserRequest;
import org.springframework.http.ResponseEntity;

public interface UserServiceInterface {
    void userRegistration(UserRequest userRequest);

    ResponseEntity<?> login(LoginRequest loginRequest);
}
