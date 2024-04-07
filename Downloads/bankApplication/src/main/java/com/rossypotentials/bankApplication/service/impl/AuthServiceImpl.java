package com.rossypotentials.bankApplication.service.impl;

import com.rossypotentials.bankApplication.domain.entity.UserEntity;
import com.rossypotentials.bankApplication.domain.enums.Roles;
import com.rossypotentials.bankApplication.infrastructure.config.JWTTokenProvider;
import com.rossypotentials.bankApplication.payload.request.EmailDetails;
import com.rossypotentials.bankApplication.payload.request.LoginRequest;
import com.rossypotentials.bankApplication.payload.request.UserRequest;
import com.rossypotentials.bankApplication.payload.response.AccountInfo;
import com.rossypotentials.bankApplication.payload.response.ApiResponse;
import com.rossypotentials.bankApplication.payload.response.BankResponse;
import com.rossypotentials.bankApplication.payload.response.JWTAuthResponse;
import com.rossypotentials.bankApplication.repository.UserRepository;
import com.rossypotentials.bankApplication.service.AuthService;
import com.rossypotentials.bankApplication.service.EmailService;
import com.rossypotentials.bankApplication.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BankResponse registerUser(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .address(userRequest.getAddress())
                .email(userRequest.getEmail())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .roles(Roles.USER)
                .status("ACTIVE")
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .build();

        UserEntity saveUser = userRepository.save(newUser);

        //send Email Alert Builder
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("CONGRATULATIONS! Your Account has been successfully created.\n\nYour Account Details:\n" +
                        "Account Name: " + saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherName() +
                        "\nAccount Number: " + saveUser.getAccountNumber() + "\n\nClick <a href=\"https://www.google.com\">here</a> to visit our Official website.")
                .build();


        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + " " +
                                saveUser.getLastName() + " " +
                                saveUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse<JWTAuthResponse>> login(LoginRequest loginRequest) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(loginRequest.getEmail());
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("LOGIN ALERT")
                .recipient(loginRequest.getEmail())
                .messageBody("You are logged into your account. If you did not initiate this request, please contact Customer service for support.")
                .build();
        emailService.sendEmailAlert(loginAlert);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        UserEntity userEntity = userEntityOptional.get();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                "Login Successful",
                                JWTAuthResponse.builder()
                                        .accessToken(token)
                                        .tokenType("Bearer")
                                        .id(userEntity.getId())
                                        .email(userEntity.getEmail())
                                        .gender(userEntity.getGender())
                                        .firstName(userEntity.getFirstName())
                                        .lastName(userEntity.getLastName())
                                        .profilePicture(userEntity.getProfilePicture())
                                        .role(userEntity.getRoles())
                                        .build()
                        )
                );
    }
}
