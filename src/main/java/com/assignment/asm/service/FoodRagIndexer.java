package com.assignment.asm.service;

import com.assignment.asm.entity.Food;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Chuyển dữ liệu món ăn trong SQL Server thành các document có embedding.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        name = "app.ai.enabled",
        havingValue = "true"
)
public class FoodRagIndexer {

    private final FoodService foodService;
    private final VectorStore vectorStore;

    /**
     * Chạy sau DataInitializer để chắc chắn dữ liệu mẫu đã có trong database.
     */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional(readOnly = true)
    public void indexMenu() {
        List<Document> documents = foodService.getAllFood()
                .stream()
                .filter(food -> food != null
                        && food.getId() != null
                        && Boolean.TRUE.equals(food.getStatus()))
                .map(this::toDocument)
                .toList();

        if (documents.isEmpty()) {
            log.info("RAG index không có món ăn nào để lập chỉ mục.");
            return;
        }

        vectorStore.add(documents);
        log.info("Đã lập RAG index cho {} món ăn.", documents.size());
    }

    private Document toDocument(Food food) {
        String category = food.getCategory() == null
                || food.getCategory().getName() == null
                ? "Chưa phân loại"
                : normalize(food.getCategory().getName());

        String description = normalize(food.getDescription());
        String name = normalize(food.getName());
        double price = food.getPrice() == null ? 0D : food.getPrice();
        int quantity = food.getQuantity() == null ? 0 : food.getQuantity();

        String text = """
                Tên món: %s
                Mô tả: %s
                Giá: %.0f VNĐ
                Số lượng còn lại: %d
                Danh mục: %s
                """
                .formatted(
                        name,
                        description.isBlank() ? "Không có mô tả" : description,
                        price,
                        quantity,
                        category
                );

        return Document.builder()
                .id("food-" + food.getId())
                .text(text)
                .metadata("foodId", food.getId())
                .metadata("category", category)
                .metadata("status", true)
                .build();
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replaceAll("[\\r\\n\\t]+", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }
}
