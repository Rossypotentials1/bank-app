package com.rossypotentials.bankApplication.service;

import com.rossypotentials.bankApplication.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {
    ResponseEntity<BankResponse<String>> uploadFilePicture(MultipartFile multipartFile);
}
