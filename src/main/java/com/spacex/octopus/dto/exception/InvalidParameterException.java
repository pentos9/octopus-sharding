package com.spacex.octopus.dto.exception;

import com.spacex.octopus.constants.ErrorConstants;
import lombok.Data;

@Data
public class InvalidParameterException extends RuntimeException {
    private String code = ErrorConstants.INVALID_PARAMETER_EXCEPTION;
    private String message;

    public InvalidParameterException(String message) {
        this.message = message;
    }

    public InvalidParameterException() {
        this.message = "参数不正确";
    }
}
