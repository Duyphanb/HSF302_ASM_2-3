package com.assignment.asm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.ai.enabled=false",
        "spring.ai.model.chat=none",
        "spring.ai.model.embedding.text=none",
        "spring.ai.google.genai.api-key="
})
@AutoConfigureMockMvc
class AiControllerIntegrationTests {

    private final MockMvc mockMvc;

    @Autowired
    AiControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void statusExplainsHowToConfigureMissingApiKey() throws Exception {
        mockMvc.perform(get("/api/chat/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.message", containsString("GEMINI_API_KEY")))
                .andExpect(jsonPath("$.message", containsString(".env")));
    }

    @Test
    void chatReturnsServiceUnavailableWhenApiKeyIsMissing() throws Exception {
        mockMvc.perform(
                        post("/api/chat")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "message": "Goi y mot mon hai san"
                                        }
                                        """)
                )
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.answer").isEmpty())
                .andExpect(jsonPath("$.error", containsString("GEMINI_API_KEY")))
                .andExpect(jsonPath("$.error", containsString(".env")));
    }
}
