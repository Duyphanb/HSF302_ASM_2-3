package com.assignment.asm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dữ liệu JSON mà frontend gửi tới {@code POST /api/chat}.
 */
public record ChatRequest(

        @NotBlank(message = "Tin nhắn không được để trống")
        @Size(
                max = 500,
                message = "Tin nhắn không được vượt quá 500 ký tự"
        )
        String message

) {
}
