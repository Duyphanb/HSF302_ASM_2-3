package com.assignment.asm.service;

/**
 * Service xử lý câu hỏi gửi từ AI Chatbox.
 */
public interface AiService {

    /**
     * Gửi câu hỏi tới hệ thống AI.
     *
     * @param message câu hỏi của người dùng
     * @return câu trả lời hiển thị trên Chatbox
     */
    String chat(String message);
}
