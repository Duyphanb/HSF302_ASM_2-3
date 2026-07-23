package com.assignment.asm.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình ChatClient dùng để gọi Google Gemini thông qua Spring AI.
 *
 * <p>Bean này chỉ được tạo khi {@code spring.ai.model.chat=google-genai}. Nhờ đó ứng dụng
 * vẫn có thể khởi động bình thường khi chưa có Gemini API key.</p>
 */
@Configuration
public class AiConfig {

    @Bean
    @ConditionalOnProperty(
            name = "spring.ai.model.chat",
            havingValue = "google-genai"
    )
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
