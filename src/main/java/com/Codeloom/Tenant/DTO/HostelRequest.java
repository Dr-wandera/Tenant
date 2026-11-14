package com.Codeloom.Tenant.DTO;

import com.Codeloom.Tenant.Enum.Location;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class HostelRequest {

    @NotBlank(message = "Hostel name required")
    private java.lang.String hostelName;

    @NotBlank(message = "Hostel photo required")
    private java.lang.String hostelImage;

    @NotBlank(message = "Address required")
    private java.lang.String address;

    @NotBlank(message = "Hostel description required")
    private java.lang.String hostelDescription;

    @NotBlank(message = "Payment details required")
    private java.lang.String paymentDetails;

    @NotNull(message = "Location required")
    private Location location;

    @NotNull(message = "Total rooms required")
    private Integer totalRooms;

    private java.lang.String idNumber;
}
