package com.elearning.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Column(nullable = false)
    private BigDecimal discountPercent;

    @Column(nullable = false)
    private BigDecimal maxDiscountAmount;

    @Column(nullable = false)
    private Integer minCourseCount;

    @Column(nullable = false)
    private Boolean isActive = true;

    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}
