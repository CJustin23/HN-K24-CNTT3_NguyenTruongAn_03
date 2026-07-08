package com.elearning.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private VoucherDTO appliedVoucher;
    private Integer courseCount;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private Boolean voucherApplied;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
