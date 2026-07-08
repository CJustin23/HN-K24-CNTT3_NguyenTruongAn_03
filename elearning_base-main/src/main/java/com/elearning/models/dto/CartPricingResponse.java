package com.elearning.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartPricingResponse {

    private BigDecimal totalOriginalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
}
