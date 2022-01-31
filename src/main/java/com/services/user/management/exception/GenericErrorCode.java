package com.services.user.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenericErrorCode {

    UnexpectedError("Unable to process request"),
    InvalidRequest("Invalid request parameters"),
    Unauthorized("Unauthorized request"),
    Forbidden("Forbidden request"),
    DataNotFound("No data found");

    private String message;
}
