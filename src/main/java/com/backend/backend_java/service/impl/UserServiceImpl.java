package com.backend.backend_java.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.backend.backend_java.dto.request.AddressDTO;
import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.dto.response.PageResponse;
import com.backend.backend_java.dto.response.UserDetailResponse;
import com.backend.backend_java.exception.ResourceNotFoundException;
import com.backend.backend_java.model.Address;
import com.backend.backend_java.model.User;
import com.backend.backend_java.repository.UserRepository;
import com.backend.backend_java.service.UserService;
import com.backend.backend_java.util.UserStatus;
import static com.backend.backend_java.util.AppConstant.SORT_BY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

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
        User user = getUserById(userId);
        if (!request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());
        }
        user.setPhone(request.getPhone());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setGender(request.getGender());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setAddresses(convertAddressToEntity(request.getAddresses()));
        user.setStatus(request.getStatus());
        userRepository.save(user);

        log.info("User updated successfully");
    };

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
        log.info("User status changed successfully");
    };

    @Override
    public void deleteUser(long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
        log.info("User deleted successfully");
    };

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

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        return UserDetailResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUsers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(pageable);
        return users.stream().map(user -> UserDetailResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build()).toList();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        return convertToPageResponse(users, pageable);
    }

    // @Override
    // public PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int
    // pageSize, String... sortBy) {
    // Pageable pageable = PageRequest.of(pageNo, pageSize);
    // Page<User> users = userRepository.findAll(pageable);
    // return users.stream().map(user -> UserDetailResponse.builder()
    // .email(user.getEmail())
    // .phone(user.getPhone())
    // .userName(user.getUsername())
    // .firstName(user.getFirstName())
    // .lastName(user.getLastName())
    // .build()).toList();
    // }

    private PageResponse<?> convertToPageResponse(Page<User> users, Pageable pageable) {
        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build()).toList();
        return PageResponse.builder()
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .totalElement(users.getTotalElements())
                .data(response)
                .build();
    }
}