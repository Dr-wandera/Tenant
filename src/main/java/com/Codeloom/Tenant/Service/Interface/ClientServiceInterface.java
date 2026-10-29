package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.ClientRequest;

public interface ClientServiceInterface {
    void updatedClientDetails(String clientId, ClientRequest clientRequest);
}
