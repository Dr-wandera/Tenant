package com.Codeloom.Tenant.DTO;

import com.Codeloom.Tenant.Models.Clients;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomResponse {
    private String roomNumber;
    private BigDecimal monthlyRent;
    private String roomImage;
    private TenantResponse tenant;

}
