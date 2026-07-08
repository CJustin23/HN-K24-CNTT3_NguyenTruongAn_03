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
public class CartItemDTO {

    private Long id;
    private Long courseId;
    private String courseTitle;
    private BigDecimal coursePrice;
    private LocalDateTime addedAt;
}
