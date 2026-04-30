package com.intergotelecom.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.intergotelecom.enums.ErrorCodeEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ErrorResponseDTO {
    private int status;

    @JsonProperty("error_code")
    private ErrorCodeEnum errorCode;

    private String message;

    private LocalDateTime timestamp;
}
