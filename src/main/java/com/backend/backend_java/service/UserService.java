package com.backend.backend_java.service;

import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.dto.response.PageResponse;
import com.backend.backend_java.dto.response.UserDetailResponse;
import com.backend.backend_java.util.UserStatus;

import java.util.List;

public interface UserService {

    int addUser(UserRequestDTO request);

    long saveUser(UserRequestDTO request);

    void updateUser(long userId, UserRequestDTO request);

    void changeStatus(long userId, UserStatus status);

    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    List<UserDetailResponse> getAllUsers(int pageNo, int pageSize);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);

    // PageResponse<?> getAllUsersWithSortByMultipleColumns(int pageNo, int
    // pageSize, String... sorts);

    // PageResponse<?> getAllUsersAndSearchWithPagingAndSorting(int pageNo, int
    // pageSize, String search, String sortBy);

    PageResponse<?> getAllUserWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy);

    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String address, String... search);

    // PageResponse<?> advanceSearchWithSpecifications(Pageable pageable, String[]
    // user, String[] address);
}
