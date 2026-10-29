package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.ClientRequest;
import com.Codeloom.Tenant.Models.Clients;
import com.Codeloom.Tenant.Repository.ClientRepository;
import com.Codeloom.Tenant.Service.Interface.ClientServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientServiceImplementation implements ClientServiceInterface {
    private final ClientRepository clientRepository;
    @Override
    public void updatedClientDetails(String clientId, ClientRequest clientRequest) {

        Clients clients =clientRepository.findByClientId(clientId)
                .orElseThrow(()->new RuntimeException("Client Not Found"));

        if (Objects.nonNull(clientRequest.getClientType())&&!"".equalsIgnoreCase(String.valueOf(clientRequest.getClientType()))){
            clients.setClientType(clientRequest.getClientType());
        }
        clientRepository.save(clients);

    }
}
