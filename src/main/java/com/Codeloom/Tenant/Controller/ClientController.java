package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.ClientRequest;
import com.Codeloom.Tenant.Service.Implem.ClientServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client")
public class ClientController {
    private final ClientServiceImplementation clientServiceImplementation;

    @PutMapping("/update/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public String updatedClientDetails(@PathVariable String clientId, @RequestBody ClientRequest clientRequest){
        clientServiceImplementation.updatedClientDetails(clientId, clientRequest);
        return "Client Details Updated Successfully";

    }
}
