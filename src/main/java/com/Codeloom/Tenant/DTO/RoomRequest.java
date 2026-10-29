package com.Codeloom.Tenant.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private String roomNumber;
    private BigDecimal monthlyRent;
    private String roomImage;
}
