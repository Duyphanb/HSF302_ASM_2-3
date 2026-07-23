package com.assignment.asm.controller;

import com.assignment.asm.dto.ChatRequest;
import com.assignment.asm.dto.ChatResponse;
import com.assignment.asm.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API nhận câu hỏi từ AI Chatbox.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request
    ) {
        String answer = aiService.chat(request.message());

        return ResponseEntity.ok(
                new ChatResponse(answer)
        );
    }
}
