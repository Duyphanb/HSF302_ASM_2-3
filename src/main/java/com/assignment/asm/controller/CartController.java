package com.assignment.asm.controller;

import com.assignment.asm.dto.CartLine;
import com.assignment.asm.entity.Food;
import com.assignment.asm.service.FoodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final String CART_KEY = "cart";
    private final FoodService foodService;

    public CartController(FoodService foodService) {
        this.foodService = foodService;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> getCart(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    @PostMapping("/add")
    public String add(@RequestParam Integer foodId,
                      @RequestParam(defaultValue = "1") Integer quantity,
                      HttpSession session) {
        Map<Integer, Integer> cart = getCart(session);
        cart.merge(foodId, Math.max(quantity, 1), Integer::sum);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer foodId,
                         @RequestParam Integer quantity,
                         HttpSession session) {
        Map<Integer, Integer> cart = getCart(session);
        if (quantity == null || quantity < 1) cart.remove(foodId);
        else cart.put(foodId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Integer foodId, HttpSession session) {
        getCart(session).remove(foodId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clear(HttpSession session) {
        getCart(session).clear();
        return "redirect:/cart";
    }

    @GetMapping
    public String view(HttpSession session, Model model) {
        Map<Integer, Integer> cart = getCart(session);
        List<CartLine> lines = cart.entrySet().stream()
                .map(entry -> foodService.getFoodByName(entry.getKey()))
                .filter(Objects::nonNull)
                .map(food -> new CartLine(food, cart.get(food.getId()),
                        BigDecimal.valueOf(food.getPrice()).multiply(BigDecimal.valueOf(cart.get(food.getId())))))
                .collect(Collectors.toList());
        BigDecimal total = lines.stream().map(CartLine::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("cartLines", lines);
        model.addAttribute("cartTotal", total);
        model.addAttribute("cartCount", cart.values().stream().mapToInt(Integer::intValue).sum());
        return "cart/view";
    }
}
