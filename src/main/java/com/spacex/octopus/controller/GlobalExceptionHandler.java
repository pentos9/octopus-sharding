package com.spacex.octopus.controller;

import com.spacex.octopus.constants.ErrorConstants;
import com.spacex.octopus.dto.exception.ErrorInfoDTO;
import com.spacex.octopus.dto.exception.InvalidParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = InvalidParameterException.class)
    public ErrorInfoDTO invalidParameterExceptionHandler(HttpServletRequest req, InvalidParameterException invalidParameterException) {
        ErrorInfoDTO errorInfoDTO = new ErrorInfoDTO();
        errorInfoDTO.setErrorCode(invalidParameterException.getCode());
        errorInfoDTO.setErrorMessage(invalidParameterException.getMessage());
        return errorInfoDTO;
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ErrorInfoDTO systemExceptionHandler(HttpServletRequest req, Exception exception) {
        ErrorInfoDTO errorInfoDTO = new ErrorInfoDTO();
        errorInfoDTO.setErrorCode(ErrorConstants.SYSTEM_EXCEPTION);
        errorInfoDTO.setErrorMessage("system error");
        return errorInfoDTO;
    }
}
