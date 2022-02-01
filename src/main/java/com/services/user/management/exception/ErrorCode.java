package com.services.user.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    VRTCUM0001("name is mandatory", "NotBlank.user.name"),
    VRTCUM0002("email is mandatory", "NotBlank.user.email"),
    VRTCUM0003("email address is invalid", "Email.user.email"),
    VRTCUM0004("phone is mandatory", "NotBlank.user.phone"),
    VRTCUM0005("", "Min.user.age"),
    ERTCUM0001("User not found", "");

    private static Map<String, ErrorCode> messageKeyErrorCodeMap = Arrays.stream(values())
            .filter(errorCode -> StringUtils.isNotBlank(errorCode.validationKey))
            .collect(Collectors.toMap(ErrorCode::getValidationKey, Function.identity()));
    private String message;
    private String validationKey;

    public static ErrorCode findByValidationKey(String validationType){
        return messageKeyErrorCodeMap.get(validationType);
    }
}
