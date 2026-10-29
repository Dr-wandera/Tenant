package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.DTO.RoomRequest;
import com.Codeloom.Tenant.DTO.RoomResponse;
import com.Codeloom.Tenant.Enum.RoomStatus;
import com.Codeloom.Tenant.Models.Hostels;
import com.Codeloom.Tenant.Models.Rooms;
import com.Codeloom.Tenant.Repository.HostelRepository;
import com.Codeloom.Tenant.Repository.RoomsRepository;
import com.Codeloom.Tenant.Service.Interface.RoomsServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoomServiceImplementation implements RoomsServiceInterface {

    private final HostelRepository hostelRepository;
    private final RoomsRepository roomsRepository;

    @Override
    public void addRoomsToHostels(String hostelId,RoomRequest roomRequest) {
        Hostels hostel = hostelRepository.findByHostelId(hostelId)
                .orElseThrow(() ->
                        new RuntimeException("Hostel not found"));

        Rooms rooms=Rooms.builder()
                .hostel(hostel)
                .monthlyRent(roomRequest.getMonthlyRent())
                .roomNumber(roomRequest.getRoomNumber())
                .roomPhoto(roomRequest.getRoomImage())
                .status(RoomStatus.AVAILABLE)
                .build();
        roomsRepository.save(rooms);

    }

    //gets all rooms not occupied
    @Override
    public List<RoomResponse> unOccupiedRoomPerHostel(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return roomsRepository.findAll(pageable)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deleteRoom(String roomId) {
        Rooms room =roomsRepository.findByRoomId(String.valueOf(roomId))
                .orElseThrow(()->new RuntimeException("Room not found"));

        roomsRepository.delete(room);

    }

    @Override
    public void updateRoomDetails(String roomId, RoomRequest roomRequest) {
        Rooms room =roomsRepository.findByRoomId(roomId)
                .orElseThrow(()->new RuntimeException("Room not found"));

        if (Objects.nonNull(roomRequest.getMonthlyRent()) && !"".equalsIgnoreCase(String.valueOf(roomRequest.getMonthlyRent()))){
            room.setMonthlyRent(roomRequest.getMonthlyRent());
        }

        if (Objects.nonNull(roomRequest.getRoomNumber()) && !"".equalsIgnoreCase(roomRequest.getRoomNumber())){
            room.setRoomNumber(roomRequest.getRoomNumber());
        }
        if (Objects.nonNull(roomRequest.getRoomImage()) && !"".equalsIgnoreCase(roomRequest.getRoomImage())){
            room.setRoomPhoto(roomRequest.getRoomImage());
        }

        roomsRepository.save(room);

    }

    //get room by room number
    @Override
    public RoomResponse getRoomByRoomNumber(String roomNumber) {
        Rooms rooms= roomsRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()->new RuntimeException("Room not found"));
        return toResponse(rooms);

    }

    //this vacates the room after tenant relocate the room
    @Override
    public void vacateRoom(String roomId) {
        Rooms room = roomsRepository.findByRoomId(roomId)
                .orElseThrow(()->new RuntimeException("Room not found"));

        room.setStatus(RoomStatus.AVAILABLE);
        roomsRepository.save(room);
    }

    private RoomResponse toResponse(Rooms rooms) {
        return RoomResponse.builder()
                .roomNumber(rooms.getRoomNumber())
                .monthlyRent(rooms.getMonthlyRent())
                .roomImage(rooms.getRoomPhoto())
                .build();
    }
}
