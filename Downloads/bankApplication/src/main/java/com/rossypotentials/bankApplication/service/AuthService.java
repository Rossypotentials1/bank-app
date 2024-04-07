package com.rossypotentials.bankApplication.service;

import com.rossypotentials.bankApplication.payload.request.LoginRequest;
import com.rossypotentials.bankApplication.payload.request.UserRequest;
import com.rossypotentials.bankApplication.payload.response.ApiResponse;
import com.rossypotentials.bankApplication.payload.response.BankResponse;
import com.rossypotentials.bankApplication.payload.response.JWTAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    BankResponse registerUser(UserRequest userRequest);
    ResponseEntity<ApiResponse<JWTAuthResponse>> login (LoginRequest loginRequest);
}
