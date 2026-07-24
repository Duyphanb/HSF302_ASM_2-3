package com.assignment.asm.controller;

import com.assignment.asm.entity.Order;
import com.assignment.asm.entity.Payment;
import com.assignment.asm.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${payment.qr.bank-bin:}")
    private String qrBankBin;

    @Value("${payment.qr.account-number:}")
    private String qrAccountNumber;

    @Value("${payment.qr.account-name:}")
    private String qrAccountName;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Map<Integer, Integer> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/cart";
        model.addAttribute("cart", cart);
        return "payment/checkout";
    }

    @PostMapping("/checkout")
    public String createOrder(HttpSession session) {
        Map<Integer, Integer> cart = getCart(session);
        Order order = paymentService.createOrder(cart);
        cart.clear();
        return "redirect:/payment/" + order.getId();
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getCart(HttpSession session) {
        Object current = session.getAttribute("cart");
        if (current instanceof Map<?, ?> existing) {
            return (Map<Integer, Integer>) existing;
        }
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        session.setAttribute("cart", cart);
        return cart;
    }

    @GetMapping("/payment/{orderId}")
    public String paymentPage(@PathVariable Long orderId, Model model) {
        Payment payment = paymentService.getPaymentForOrder(orderId);
        model.addAttribute("payment", payment);
        String orderInfo = "Thanh toan don hang " + orderId;
        String qrUrl = "https://img.vietqr.io/image/" + qrBankBin + "-" + qrAccountNumber
                + "-compact2.png?amount=" + payment.getAmount().longValue()
                + "&addInfo=" + URLEncoder.encode(orderInfo, StandardCharsets.UTF_8)
                + "&accountName=" + URLEncoder.encode(qrAccountName, StandardCharsets.UTF_8);
        model.addAttribute("qrUrl", qrUrl);
        model.addAttribute("qrConfigured", !qrBankBin.isBlank() && !qrAccountNumber.isBlank() && !qrAccountName.isBlank());
        return "payment/mock-payment";
    }

    @PostMapping("/payment/{orderId}/process")
    public String processPayment(@PathVariable Long orderId,
                                 @RequestParam String action) {
        paymentService.processMockPayment(orderId, action);
        return "redirect:/payment/" + orderId + "/result";
    }

    @GetMapping("/payment/{orderId}/result")
    public String paymentResult(@PathVariable Long orderId, Model model) {
        Payment payment = paymentService.getPaymentForOrder(orderId);
        model.addAttribute("payment", payment);
        return "payment/result";
    }
}
