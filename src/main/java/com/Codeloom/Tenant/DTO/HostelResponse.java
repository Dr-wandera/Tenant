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
    private java.lang.String hostelName;
    private java.lang.String hostelPhoto;
    private java.lang.String address;
    private java.lang.String hostelDescription;
    private Location location;
}
