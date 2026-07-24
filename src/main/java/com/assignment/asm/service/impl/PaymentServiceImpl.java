package com.assignment.asm.service.impl;

import com.assignment.asm.entity.*;
import com.assignment.asm.repository.FoodRepository;
import com.assignment.asm.repository.OrderRepository;
import com.assignment.asm.repository.PaymentRepository;
import com.assignment.asm.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(FoodRepository foodRepository,
                              OrderRepository orderRepository,
                              PaymentRepository paymentRepository) {
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Order createOrder(Map<Integer, Integer> cart) {
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng đang trống");
        }

        Order order = Order.builder().status(OrderStatus.PENDING).build();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Integer quantity = entry.getValue();
            if (quantity == null || quantity < 1) {
                throw new IllegalArgumentException("Số lượng không hợp lệ");
            }

            Food food = foodRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn"));
            if (food.getQuantity() < quantity) {
                throw new IllegalArgumentException("Món " + food.getName() + " không đủ số lượng");
            }

            BigDecimal unitPrice = BigDecimal.valueOf(food.getPrice());
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            order.addItem(OrderItem.builder()
                    .food(food)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build());
            total = total.add(subtotal);
        }

        // Điền dữ liệu tương thích cho schema orders cũ nếu database đang dùng ddl-auto=update.
        OrderItem firstItem = order.getItems().get(0);
        order.setFood(firstItem.getFood());
        order.setQuantity(firstItem.getQuantity());
        order.setTotalAmount(total);
        order = orderRepository.save(order);

        paymentRepository.save(Payment.builder()
                .order(order)
                .amount(total)
                .transactionCode("MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(PaymentStatus.PENDING)
                .build());
        return order;
    }

    @Override
    @Transactional
    public Payment getPaymentForOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch"));
    }

    @Override
    @Transactional
    public Payment processMockPayment(Long orderId, String action) {
        Payment payment = getPaymentForOrder(orderId);
        Order order = payment.getOrder();
        PaymentStatus paymentStatus;
        OrderStatus orderStatus;
        switch (action) {
            case "success" -> {
                paymentStatus = PaymentStatus.SUCCESS;
                orderStatus = OrderStatus.PAID;
            }
            case "failed" -> {
                paymentStatus = PaymentStatus.FAILED;
                orderStatus = OrderStatus.FAILED;
            }
            case "cancel" -> {
                paymentStatus = PaymentStatus.CANCELLED;
                orderStatus = OrderStatus.CANCELLED;
            }
            default -> throw new IllegalArgumentException("Thao tác thanh toán không hợp lệ");
        }
        payment.setStatus(paymentStatus);
        order.setStatus(orderStatus);
        paymentRepository.save(payment);
        orderRepository.save(order);
        return payment;
    }
}
