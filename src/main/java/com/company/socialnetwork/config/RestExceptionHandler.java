package com.company.socialnetwork.config;

import com.company.socialnetwork.exceptions.SocialNetworkAppException;
import com.company.socialnetwork.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = { SocialNetworkAppException.class })
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(SocialNetworkAppException ex) {
        log.error("Unhandled error", ex);
        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorDto.builder().message(ex.getMessage()).build());
    }
}
