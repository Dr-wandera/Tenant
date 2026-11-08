package com.Codeloom.Tenant.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomNumber;
    private BigDecimal monthlyRent;
    private String roomImage;
    private TenantResponse tenant;

}
