package com.assignment.asm.service;

import com.assignment.asm.entity.Order;
import com.assignment.asm.entity.Payment;

import java.util.Map;

public interface PaymentService {
    Order createOrder(Map<Integer, Integer> cart);

    Payment getPaymentForOrder(Long orderId);

    Payment processMockPayment(Long orderId, String action);
}
