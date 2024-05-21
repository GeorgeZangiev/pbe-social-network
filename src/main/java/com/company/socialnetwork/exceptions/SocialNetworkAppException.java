package com.company.socialnetwork.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SocialNetworkAppException extends RuntimeException {

    private final HttpStatus status;

    public SocialNetworkAppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
