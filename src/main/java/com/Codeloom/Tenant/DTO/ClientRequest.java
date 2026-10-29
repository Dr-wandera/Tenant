package com.Codeloom.Tenant.DTO;

import com.Codeloom.Tenant.Enum.ClientType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRequest {
    private ClientType clientType;
}
