package com.Codeloom.Tenant.DTO;

import com.Codeloom.Tenant.Enum.Location;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class HostelRequest {

    @NotBlank(message = "Hostel name required")
    private String hostelName;

    @NotBlank(message = "Hostel photo required")
    private String hostelPhoto;

    @NotBlank(message = "Address required")
    private String address;

    @NotBlank(message = "Hostel description required")
    private String hostelDescription;

    @NotBlank(message = "Payment details required")
    private String paymentDetails;

    @NotNull(message = "Location required")
    private Location location;

    @NotNull(message = "Total rooms required")
    private Integer totalRooms;

    private Long caretakerId;
}
