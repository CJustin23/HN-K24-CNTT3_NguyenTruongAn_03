package com.elearning.controllers;

import com.elearning.advice.ApiResponse;
import com.elearning.models.dto.ApplyVoucherRequest;
import com.elearning.models.dto.CartPricingResponse;
import com.elearning.models.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/pricing")
    public ResponseEntity<ApiResponse<CartPricingResponse>> calculateCartPricing(
            @RequestParam(required = false) String voucherCode) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        CartPricingResponse pricing = (voucherCode != null && !voucherCode.isBlank())
                ? cartService.calculateCartPricing(userEmail, voucherCode)
                : cartService.calculateCartPricing(userEmail);

        return ResponseEntity.ok(
                ApiResponse.success(pricing, "Cart pricing calculated successfully"));
    }

    @PostMapping("/voucher")
    public ResponseEntity<ApiResponse<Void>> applyVoucher(@RequestBody ApplyVoucherRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.applyVoucher(userEmail, request.getCode());

        return ResponseEntity.ok(ApiResponse.success(null, "Voucher applied successfully"));
    }
}
