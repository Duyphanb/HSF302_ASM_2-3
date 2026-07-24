package com.assignment.asm.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình vector store dùng cho RAG.
 *
 * <p>SimpleVectorStore lưu vector trong bộ nhớ, phù hợp cho bài tập và demo.
 * Khi triển khai thật có thể thay bean này bằng một vector database mà không
 * cần đổi RagService.</p>
 */
@Configuration
@ConditionalOnProperty(
        name = "app.ai.enabled",
        havingValue = "true"
)
public class RagConfig {

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}
