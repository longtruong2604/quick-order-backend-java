package com.backend.backend_java.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.backend_java.configuration.Translator;
import com.backend.backend_java.dto.request.UserRequestDTO;
import com.backend.backend_java.dto.response.ResponseData;
import com.backend.backend_java.dto.response.ResponseError;
import com.backend.backend_java.service.UserService;
import com.backend.backend_java.util.UserStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Slf4j
@Validated
@Tag(name = "User Controller", description = "User management")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Operation(summary = "Add a new user", description = "Add a new user to the system")
    @PostMapping("/")
    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO user) {
        log.info("Request add user " + user.getFirstName());
        try {
            long userId = userService.saveUser(user);
            return new ResponseData<>(HttpStatus.CREATED.value(),
                    Translator.toLocale("user.add.success"), userId);
        } catch (Exception e) {
            log.error("errorMessage: {}", e.getMessage(), e.getCause());
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update user")
    @PutMapping("/{userId}")
    public ResponseData<?> updateUser(@PathVariable @Min(1) int userId, @Valid @RequestBody UserRequestDTO user) {
        log.info("Request update user, userId=" + userId);
        try {
            userService.updateUser(userId, user);
        } catch (Exception e) {
            log.error("errorMessage: {}", e.getMessage(), e.getCause());
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User updated successfully");
    }

    @Operation(summary = "Change user status")
    @PatchMapping("/{userId}")
    public ResponseData<?> updateStatus(@Min(1) @PathVariable int userId,
            @RequestParam UserStatus status) {
        log.info("Request change status, userId=" + userId);
        try {
            userService.changeStatus(userId, status);
        } catch (Exception e) {
            log.error("errorMessage: {}", e.getMessage(), e.getCause());
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User's status changed successfully");
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{userId}")
    public ResponseData<?> deleteUser(
            @PathVariable @Min(value = 1, message = "userId must be greater than 0") int userId) {
        log.info("Request delete userId=" + userId);
        try {
            userService.deleteUser(userId);
        } catch (Exception e) {
            log.error("errorMessage: {}", e.getMessage(), e.getCause());
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted successfully");
    }

    @Operation(summary = "Get user detail")
    @GetMapping("/{userId}")
    public ResponseData<?> getUser(@PathVariable @Min(1) int userId) {
        log.info("Request get user detail, userId=" + userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get user detail successfully",
                    userService.getUser(userId));
        } catch (Exception e) {
            log.error("errorMessage: {}", e.getMessage(), e.getCause());
            return new ResponseError<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of users per pageNo", description = "Send a request via this API to get user list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllUsersWithSortBy(@RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy) {
        log.info("Request get all of users");
        return new ResponseData<>(HttpStatus.OK.value(), "users",
                userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Search for user", description = "Send a request via this API to get user list by search string")
    @GetMapping("/search")
    public ResponseData<?> getAllUsersWithSortByColumnAndSearch(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize) {
        log.info("Request get all of users");
        return new ResponseData<>(HttpStatus.OK.value(), "users",
                userService.getAllUserWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy));
    }

    @Operation(summary = "Search for user", description = "Send a request via this API to get user list by search string")
    @GetMapping("/search-criteria")
    public ResponseData<?> advanceSearchByCriteria(
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(defaultValue = "20", required = false) String address,
            @RequestParam(defaultValue = "", required = false) String... search) {
        log.info("Request get all of users");
        return new ResponseData<>(HttpStatus.OK.value(), "users",
                userService.advanceSearchByCriteria(pageNo, pageSize, sortBy, address, search));
    }
}