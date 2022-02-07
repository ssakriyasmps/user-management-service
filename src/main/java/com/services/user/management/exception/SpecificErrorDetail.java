package com.services.user.management.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Setter(value = AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecificErrorDetail {
    private ErrorCode errorCode;
    private String message;
    private String fieldName;

    public SpecificErrorDetail(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public SpecificErrorDetail(ErrorCode errorCode, String message){
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
