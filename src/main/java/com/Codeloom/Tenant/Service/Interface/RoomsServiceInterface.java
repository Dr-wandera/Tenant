package com.Codeloom.Tenant.Service.Interface;

import com.Codeloom.Tenant.DTO.RoomRequest;
import com.Codeloom.Tenant.DTO.RoomResponse;

import java.util.List;

public interface RoomsServiceInterface {
    RoomResponse addRoomsToHostels(Long hostelId,RoomRequest roomRequest);

    List<RoomResponse> unOccupiedRoomPerHostel(Long hostelId,int page, int size);

    void deleteRoom(String roomId);

    RoomResponse updateRoomDetails(String roomId, RoomRequest roomRequest);

    RoomResponse getRoomByRoomNumber(String roomNumber);

    RoomResponse vacateRoom(String roomId);

    RoomResponse getTenantByRoomNumber(String roomNumber);

    List<RoomResponse> allRoomPerHostel(Long hostelId, int page, int size);
}
