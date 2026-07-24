package com.assignment.asm.service;

/**
 * Truy xuất dữ liệu liên quan để bổ sung context cho mô hình AI.
 */
public interface RagService {

    /**
     * Tìm các món ăn liên quan nhất tới câu hỏi và chuyển thành context.
     *
     * @param query câu hỏi của người dùng
     * @return context cho mô hình AI, hoặc chuỗi rỗng nếu không có kết quả
     */
    String retrieveContext(String query);
}
