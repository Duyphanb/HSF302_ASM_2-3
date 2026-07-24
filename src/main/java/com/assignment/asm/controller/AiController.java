package com.assignment.asm.controller;

import com.assignment.asm.dto.ChatRequest;
import com.assignment.asm.dto.ChatResponse;
import com.assignment.asm.dto.ChatStatusResponse;
import com.assignment.asm.exception.AiServiceUnavailableException;
import com.assignment.asm.service.AiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API nhận câu hỏi từ AI Chatbox.
 */
@RestController
@RequestMapping("/api/chat")
public class AiController {

    private final AiService aiService;
    private final String chatModel;
    private final String apiKey;

    @Autowired
    public AiController(
            AiService aiService,
            @Value("${spring.ai.model.chat:none}") String chatModel,
            @Value("${spring.ai.google.genai.api-key:}") String apiKey
    ) {
        this.aiService = aiService;
        this.chatModel = chatModel;
        this.apiKey = apiKey;
    }

    @GetMapping("/status")
    public ResponseEntity<ChatStatusResponse> status() {
        return ResponseEntity.ok(getAiStatus());
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request
    ) {
        ChatStatusResponse status = getAiStatus();

        if (!status.available()) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ChatResponse(null, status.message()));
        }

        try {
            String answer = aiService.chat(request.message());

            return ResponseEntity.ok(
                    new ChatResponse(answer, null)
            );
        } catch (AiServiceUnavailableException exception) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ChatResponse(null, exception.getMessage()));
        }
    }

    private ChatStatusResponse getAiStatus() {
        if (apiKey == null || apiKey.isBlank()) {
            return new ChatStatusResponse(
                    false,
                    "AI chưa được cấu hình. Hãy thêm GEMINI_API_KEY vào file .env "
                            + "và đặt SPRING_AI_MODEL_CHAT=google-genai."
            );
        }

        if (!"google-genai".equalsIgnoreCase(chatModel)) {
            return new ChatStatusResponse(
                    false,
                    "AI đang tắt. Hãy đặt SPRING_AI_MODEL_CHAT=google-genai "
                            + "trong file .env."
            );
        }

        return new ChatStatusResponse(true, "AI sẵn sàng.");
    }
}
