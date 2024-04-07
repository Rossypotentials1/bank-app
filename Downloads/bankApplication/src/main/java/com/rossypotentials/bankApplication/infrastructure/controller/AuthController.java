package com.rossypotentials.bankApplication.infrastructure.controller;

import com.rossypotentials.bankApplication.payload.request.LoginRequest;
import com.rossypotentials.bankApplication.payload.request.UserRequest;
import com.rossypotentials.bankApplication.payload.response.ApiResponse;
import com.rossypotentials.bankApplication.payload.response.BankResponse;
import com.rossypotentials.bankApplication.payload.response.JWTAuthResponse;
import com.rossypotentials.bankApplication.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Tag(name = "User Authentication Management APIs")
public class AuthController {
    private final AuthService authService;
@Operation(
        summary = "Register New User Account",
        description = "Register a new user and generate account Number"
)
    @PostMapping("register-user")

    public BankResponse createAccount(@Valid @RequestBody UserRequest userRequest){
        return authService.registerUser(userRequest);
    }
    @PostMapping("login-user")
    public ResponseEntity<ApiResponse<JWTAuthResponse>> login(@RequestBody LoginRequest loginRequest){
    return authService.login(loginRequest);
    }

}
