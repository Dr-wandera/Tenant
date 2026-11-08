package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.HostelRequest;
import com.Codeloom.Tenant.DTO.HostelResponse;
import com.Codeloom.Tenant.DTO.UserResponse;
import com.Codeloom.Tenant.Models.Bookings;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.List;

public interface HostelServiceInterface {
    HostelResponse createHostel(HostelRequest hostelRequest);

    List<HostelResponse> getAllHostelUnderLandlord();

    List<HostelResponse> getByHostelName(String hostelName);

    List<HostelResponse> allHostel(int page, int size);

    HostelResponse updateHostel(Long hostelId, HostelRequest hostelRequest);

    void deleteHostel(Long hostelId);

    List<HostelResponse> allHostelPerLandLord(int page, int size);

    List<UserResponse> getAllTenantPerHostel(Long hostelId, int page, int size);
}
