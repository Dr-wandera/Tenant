package com.Codeloom.Tenant.Controller;

import com.Codeloom.Tenant.DTO.RoomRequest;
import com.Codeloom.Tenant.DTO.RoomResponse;
import com.Codeloom.Tenant.DTO.UserResponse;
import com.Codeloom.Tenant.Service.Implem.RoomServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {
    private final RoomServiceImplementation  roomServiceImplementation;

    @PostMapping("/add/{hostelId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('LANDLORD', 'CARETAKER')")
    public String addRoom(@PathVariable String hostelId,@RequestBody RoomRequest roomRequest){
        roomServiceImplementation.addRoomsToHostels(hostelId,roomRequest);
        return "Room added successfully";
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> unOccupiedRoomPerHostel(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return roomServiceImplementation.unOccupiedRoomPerHostel(page,size);
    }
    @DeleteMapping("/delete/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER','ADMIN')")
    public String deleteRoom(@PathVariable String roomId){
        roomServiceImplementation.deleteRoom(roomId);
        return "Room deleted successfully";
    }

    @PutMapping("/update/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER','ADMIN')")
    public String updateRoom(@PathVariable String roomId,@RequestBody RoomRequest roomRequest){
        roomServiceImplementation.updateRoomDetails(roomId,roomRequest);
        return "Room updated successfully";
    }

    @GetMapping("/room_number/{roomNumber}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponse getRoomByRoomNumber(@PathVariable String roomNumber){
        return roomServiceImplementation.getRoomByRoomNumber(roomNumber);
    }

    @PatchMapping("/vacate/{roomId}")
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER','ADMIN')")
    public String vacateRoom(@PathVariable String roomId){
        roomServiceImplementation.vacateRoom(roomId);
        return "Room vacated successfully";
    }

    @GetMapping("/tenant/{roomNumber}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('LANDLORD','CARETAKER','ADMIN')")
    public RoomResponse getTenantByRoomNumber (@PathVariable String roomNumber){
        return roomServiceImplementation.getTenantByRoomNumber(roomNumber);
    }
}
