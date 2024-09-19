package com.backend.backend_java.service.impl;

import org.springframework.stereotype.Service;

import com.backend.backend_java.dto.request.AddressDTO;
import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.model.Address;
import com.backend.backend_java.model.User;
import com.backend.backend_java.repository.UserRepository;
import com.backend.backend_java.service.UserService;
import com.backend.backend_java.util.UserStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;
import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public int addUser(UserRequestDTO request) {
        return 0;
    }

    @Override
    public long saveUser(UserRequestDTO request) {
        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .type(request.getUserType())
                .addresses(convertAddressToEntity(request.getAddresses()))
                .status(request.getStatus())
                .build();
        userRepository.save(user);
        log.info("User saved successfully");
        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequestDTO request) {
    };

    @Override
    public void changeStatus(long userId, UserStatus status) {
    };

    @Override
    public void deleteUser(long userId) {
    };

    // @Override
    // public UserDetailResponse getUser(long userId) {
    // return new UserDetailResponse()
    // };

    private Set<Address> convertAddressToEntity(Set<AddressDTO> addresses) {
        Set<Address> addressEntities = new HashSet<>();
        addresses.forEach(address -> {
            Address addressEntity = Address.builder()
                    .apartmentNumber(address.getApartmentNumber())
                    .building(address.getBuilding())
                    .city(address.getCity())
                    .country(address.getCountry())
                    .floor(address.getFloor())
                    .street(address.getStreet())
                    .streetNumber(address.getStreetNumber())
                    .addressType(address.getAddressType())
                    .build();
            addressEntities.add(addressEntity);
        });

        return addressEntities;
    }
}