package com.assignment.asm.dto;

/**
 * Trạng thái cấu hình của AI Chatbox.
 */
public record ChatStatusResponse(
        boolean available,
        String message
) {
}
