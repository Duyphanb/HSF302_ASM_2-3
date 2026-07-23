package com.assignment.asm.exception;

/**
 * Báo hiệu dịch vụ AI tạm thời không thể xử lý yêu cầu.
 */
public class AiServiceUnavailableException extends RuntimeException {

    public AiServiceUnavailableException(String message) {
        super(message);
    }
}
