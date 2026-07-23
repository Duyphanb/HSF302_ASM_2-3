package com.assignment.asm.service.impl;

import com.assignment.asm.service.AiService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Service fallback được dùng khi AI chưa được bật.
 */
@Service
@ConditionalOnProperty(
        name = "app.ai.enabled",
        havingValue = "false",
        matchIfMissing = true
)
public class DisabledAIChatServiceImpl implements AiService {

    @Override
    public String chat(String message) {
        return "Tính năng AI Chatbox hiện đang tạm tắt. "
                + "Vui lòng cấu hình Gemini API key để sử dụng.";
    }
}
