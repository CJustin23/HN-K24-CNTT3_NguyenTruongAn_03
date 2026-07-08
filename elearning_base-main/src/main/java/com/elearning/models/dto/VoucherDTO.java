package com.elearning.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDTO {

    private Long id;
    private String code;
    private String description;
    private BigDecimal discountPercent;
    private BigDecimal maxDiscountAmount;
    private Integer minCourseCount;
    private Boolean isActive;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}
