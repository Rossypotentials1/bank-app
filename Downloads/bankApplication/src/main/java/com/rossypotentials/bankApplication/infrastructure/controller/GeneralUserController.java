package com.rossypotentials.bankApplication.infrastructure.controller;

import com.rossypotentials.bankApplication.payload.response.BankResponse;
import com.rossypotentials.bankApplication.service.GeneralUserService;
import com.rossypotentials.bankApplication.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class GeneralUserController {
    private final GeneralUserService generalUserService;

    @PutMapping("/profile-picture")
    public ResponseEntity<BankResponse<String>> profilePicUpload (@RequestParam MultipartFile profilePic){
        if (profilePic.getSize() > AppConstant.MAX_FILE_SIZE){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new BankResponse<>("Exceeds the required size limit"));
        }
        return generalUserService.uploadFilePicture(profilePic);
    }
}
