package com.assignment.asm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.ai.enabled=false",
        "spring.ai.model.chat=none",
        "spring.ai.model.embedding.text=none",
        "spring.ai.google.genai.api-key="
})
class AsmApplicationTests {

    @Test
    void contextLoads() {
    }

}
