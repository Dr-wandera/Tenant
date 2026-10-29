package com.Codeloom.Tenant.DTO;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomResponse {
    private String roomNumber;
    private BigDecimal monthlyRent;
    private String roomImage;
}
