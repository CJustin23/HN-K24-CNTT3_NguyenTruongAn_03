package com.elearning.models.services;

import com.elearning.exceptions.BusinessException;
import com.elearning.models.dto.CartPricingResponse;
import com.elearning.models.entities.Cart;
import com.elearning.models.entities.CartItem;
import com.elearning.models.entities.Course;
import com.elearning.models.entities.User;
import com.elearning.models.entities.Voucher;
import com.elearning.models.repositories.CartItemRepository;
import com.elearning.models.repositories.CartRepository;
import com.elearning.models.repositories.UserRepository;
import com.elearning.models.repositories.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoucherRepository voucherRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void calculateCartPricing_withoutVoucher_returnsOriginalAmount() {
        Cart cart = new Cart();
        cart.setId(1L);

        List<CartItem> items = List.of(
                cartItem(1L, new BigDecimal("1000000")),
                cartItem(2L, new BigDecimal("2000000")));

        when(cartItemRepository.findByCartId(1L)).thenReturn(items);

        CartPricingResponse result = cartService.calculateCartPricing(cart);

        assertEquals(new BigDecimal("3000000"), result.getTotalOriginalAmount());
        assertEquals(BigDecimal.ZERO, result.getDiscountAmount());
        assertEquals(new BigDecimal("3000000"), result.getFinalAmount());
    }

    @Test
    void calculateCartPricing_withValidVoucher_appliesDiscountAndCap() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setAppliedVoucher(voucher("SUMMER20", 20, 500000, 2));

        List<CartItem> items = List.of(
                cartItem(1L, new BigDecimal("2000000")),
                cartItem(2L, new BigDecimal("2000000")),
                cartItem(3L, new BigDecimal("2000000")));

        when(cartItemRepository.findByCartId(1L)).thenReturn(items);

        CartPricingResponse result = cartService.calculateCartPricing(cart);

        assertEquals(new BigDecimal("6000000"), result.getTotalOriginalAmount());
        assertEquals(new BigDecimal("500000"), result.getDiscountAmount());
        assertEquals(new BigDecimal("5500000"), result.getFinalAmount());
    }

    @Test
    void calculateCartPricing_withVoucherCodeNotFound_throwsBusinessException400() {
        User user = new User();
        user.setId(1L);
        user.setEmail("student@test.com");

        Cart cart = new Cart();
        cart.setId(1L);

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(cart));
        when(voucherRepository.findByCodeIgnoreCase("INVALID")).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.calculateCartPricing("student@test.com", "INVALID"));

        assertEquals(400, ex.getCode());
        assertEquals("Mã voucher không tồn tại", ex.getMessage());
    }

    @Test
    void calculateCartPricing_withBlankVoucherCode_throwsBusinessException400() {
        User user = new User();
        user.setId(1L);
        user.setEmail("student@test.com");

        Cart cart = new Cart();
        cart.setId(1L);

        when(userRepository.findByEmail("student@test.com")).thenReturn(Optional.of(user));
        when(cartRepository.findByUser_Id(1L)).thenReturn(Optional.of(cart));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.calculateCartPricing("student@test.com", "   "));

        assertEquals(400, ex.getCode());
        assertEquals("Mã voucher không hợp lệ", ex.getMessage());
    }

    @Test
    void calculateCartPricing_withInsufficientCourses_throwsBusinessException400() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setAppliedVoucher(voucher("SUMMER20", 20, 500000, 2));

        when(cartItemRepository.findByCartId(1L)).thenReturn(
                List.of(cartItem(1L, new BigDecimal("1000000"))));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> cartService.calculateCartPricing(cart));

        assertEquals(400, ex.getCode());
        assertEquals("Phải mua ít nhất 2 khóa học để áp dụng mã này", ex.getMessage());
    }

    private CartItem cartItem(Long courseId, BigDecimal price) {
        Course course = new Course();
        course.setId(courseId);
        course.setPrice(price);

        CartItem item = new CartItem();
        item.setCourse(course);
        return item;
    }

    private Voucher voucher(String code, int percent, int maxDiscount, int minCourses) {
        Voucher voucher = new Voucher();
        voucher.setCode(code);
        voucher.setDiscountPercent(BigDecimal.valueOf(percent));
        voucher.setMaxDiscountAmount(BigDecimal.valueOf(maxDiscount));
        voucher.setMinCourseCount(minCourses);
        voucher.setIsActive(true);
        return voucher;
    }
}
