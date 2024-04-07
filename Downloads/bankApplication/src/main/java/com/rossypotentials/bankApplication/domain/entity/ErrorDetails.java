package com.rossypotentials.bankApplication.domain.entity;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String debugMessage;
    private HttpStatus status;
    private String  dateTime;
}
