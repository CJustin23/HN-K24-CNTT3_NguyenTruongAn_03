package com.elearning.models.services;

import com.elearning.exceptions.BusinessException;
import com.elearning.models.dto.CartPricingResponse;
import com.elearning.models.entities.Cart;
import com.elearning.models.entities.CartItem;
import com.elearning.models.entities.User;
import com.elearning.models.entities.Voucher;
import com.elearning.models.repositories.CartItemRepository;
import com.elearning.models.repositories.CartRepository;
import com.elearning.models.repositories.UserRepository;
import com.elearning.models.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;

    @Transactional
    public void applyVoucher(String userEmail, String voucherCode) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new BusinessException(404, "Cart not found"));

        Voucher voucher = resolveVoucher(voucherCode);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        validateVoucherConditions(voucher, cartItems.size());

        cart.setAppliedVoucher(voucher);
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public CartPricingResponse calculateCartPricing(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new BusinessException(404, "Cart not found"));

        return calculateCartPricing(cart);
    }

    @Transactional(readOnly = true)
    public CartPricingResponse calculateCartPricing(String userEmail, String voucherCode) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BusinessException(404, "User not found"));

        Cart cart = cartRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new BusinessException(404, "Cart not found"));

        Voucher voucher = resolveVoucher(voucherCode);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        validateVoucherConditions(voucher, cartItems.size());

        return buildPricingResponse(cartItems, voucher);
    }

    public CartPricingResponse calculateCartPricing(Cart cart) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        Voucher voucher = cart.getAppliedVoucher();

        if (voucher != null) {
            validateVoucherConditions(voucher, cartItems.size());
        }

        return buildPricingResponse(cartItems, voucher);
    }

    private CartPricingResponse buildPricingResponse(List<CartItem> cartItems, Voucher voucher) {
        BigDecimal totalOriginalAmount = cartItems.stream()
                .map(item -> item.getCourse().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountAmount = BigDecimal.ZERO;

        if (voucher != null) {
            BigDecimal rawDiscount = totalOriginalAmount
                    .multiply(voucher.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);

            discountAmount = rawDiscount.min(voucher.getMaxDiscountAmount());
        }

        BigDecimal finalAmount = totalOriginalAmount.subtract(discountAmount);

        return CartPricingResponse.builder()
                .totalOriginalAmount(totalOriginalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .build();
    }

    private Voucher resolveVoucher(String voucherCode) {
        if (voucherCode == null || voucherCode.isBlank()) {
            throw new BusinessException(400, "Mã voucher không hợp lệ");
        }

        return voucherRepository.findByCodeIgnoreCase(voucherCode.trim())
                .orElseThrow(() -> new BusinessException(400, "Mã voucher không tồn tại"));
    }

    private void validateVoucherConditions(Voucher voucher, int courseCount) {
        if (!Boolean.TRUE.equals(voucher.getIsActive())) {
            throw new BusinessException(400, "Mã giảm giá không còn hoạt động");
        }

        LocalDateTime now = LocalDateTime.now();

        if (voucher.getValidFrom() != null && now.isBefore(voucher.getValidFrom())) {
            throw new BusinessException(400, "Mã giảm giá chưa có hiệu lực");
        }

        if (voucher.getValidTo() != null && now.isAfter(voucher.getValidTo())) {
            throw new BusinessException(400, "Mã giảm giá đã hết hạn");
        }

        if (courseCount < voucher.getMinCourseCount()) {
            throw new BusinessException(400,
                    "Phải mua ít nhất " + voucher.getMinCourseCount() + " khóa học để áp dụng mã này");
        }
    }
}
