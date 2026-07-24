package com.assignment.asm.service.impl;

import com.assignment.asm.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Tìm kiếm semantic trong vector store và tạo context cho Gemini.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "app.ai.enabled",
        havingValue = "true"
)
public class FoodRagServiceImpl implements RagService {

    private static final int TOP_K = 5;
    private static final double SIMILARITY_THRESHOLD = 0.45D;

    private final VectorStore vectorStore;

    @Override
    public String retrieveContext(String query) {
        if (query == null || query.isBlank()) {
            return "";
        }

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query.trim())
                        .topK(TOP_K)
                        .similarityThreshold(SIMILARITY_THRESHOLD)
                        .build()
        );

        if (documents == null || documents.isEmpty()) {
            return "";
        }

        return documents.stream()
                .map(Document::getText)
                .filter(Objects::nonNull)
                .filter(text -> !text.isBlank())
                .collect(Collectors.joining("\n---\n"));
    }
}
