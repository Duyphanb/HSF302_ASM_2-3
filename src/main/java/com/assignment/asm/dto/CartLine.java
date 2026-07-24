package com.assignment.asm.dto;

import com.assignment.asm.entity.Food;

import java.math.BigDecimal;

public record CartLine(Food food, Integer quantity, BigDecimal subtotal) {
}
