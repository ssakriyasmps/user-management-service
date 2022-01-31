package com.services.user.management.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Setter(value = AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetail {
    private GenericErrorCode code;
    private String message;
    private List<SpecificErrorDetails> errorMessages;

    public ErrorDetail(GenericErrorCode code, List<SpecificErrorDetails> errorMessages){
        this.code = code;
        this.message = code.getMessage();
        this.errorMessages = errorMessages;
    }
}
