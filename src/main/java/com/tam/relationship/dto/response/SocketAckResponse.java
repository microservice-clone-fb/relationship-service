package com.tam.relationship.dto.response;

import com.tam.relationship.exception.ErrorCode;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SocketAckResponse {
    boolean success;
    String message;
    Integer code;
    Object payload;

    public static SocketAckResponse success(String message) {
        return SocketAckResponse.builder()
                .success(true)
                .message(message)
                .code(1000)
                .build();
    }

    public static SocketAckResponse success(String message, Object payload) {
        return SocketAckResponse.builder()
                .success(true)
                .message(message)
                .code(1000)
                .payload(payload)
                .build();
    }

    public static SocketAckResponse failure(String message, Integer code) {
        return SocketAckResponse.builder()
                .success(false)
                .message(message)
                .code(code)
                .build();
    }

    public static SocketAckResponse failure(ErrorCode errorCode) {
        return failure(errorCode.getMessage(), errorCode.getCode());
    }

    public static SocketAckResponse failure(Throwable throwable) {
        return failure(throwable.getMessage(), 9999);
    }
}
