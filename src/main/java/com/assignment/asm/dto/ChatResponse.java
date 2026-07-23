package com.assignment.asm.dto;

/**
 * Dữ liệu JSON được API trả về cho frontend.
 */
public record ChatResponse(
        String answer,
        String error
) {
}
