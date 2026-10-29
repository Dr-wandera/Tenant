package com.Codeloom.Tenant.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpRequest {
    private String otpCode;
}
