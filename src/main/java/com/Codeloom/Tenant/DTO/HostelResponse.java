package com.Codeloom.Tenant.DTO;

import com.Codeloom.Tenant.Enum.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelResponse {
    private String hostelName;
    private Long total_rooms;
    private String hostelPhoto;
    private  String address;
    private String hostelDescription;
    private Location location;
}
