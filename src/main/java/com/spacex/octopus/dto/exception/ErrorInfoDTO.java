package com.spacex.octopus.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfoDTO {
    private String errorCode;
    private String errorMessage;
}
