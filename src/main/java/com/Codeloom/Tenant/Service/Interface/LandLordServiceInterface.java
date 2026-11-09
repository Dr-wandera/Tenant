package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.LandLordRequest;
import com.Codeloom.Tenant.DTO.LandLordResponse;

public interface LandLordServiceInterface {
    LandLordResponse updateLandlord(LandLordRequest landLordRequest);
}
