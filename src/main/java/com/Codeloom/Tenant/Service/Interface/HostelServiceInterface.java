package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.HostelRequest;
import com.Codeloom.Tenant.DTO.HostelResponse;
import com.Codeloom.Tenant.Models.Bookings;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.util.List;

public interface HostelServiceInterface {
    HostelResponse createHostel(HostelRequest hostelRequest);

    List<HostelResponse> getAllHostelUnderLandlord();

    List<HostelResponse> getByHostelName(String hostelName);

    List<HostelResponse> allHostel(int page, int size);

    HostelResponse updateHostel(String hostelId, HostelRequest hostelRequest);

    void deleteHostel(String hostelId);
}
