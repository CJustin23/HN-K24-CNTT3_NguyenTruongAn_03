package com.elearning.models.repositories;

import com.elearning.models.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndCourseId(Long cartId, Long courseId);

    boolean existsByCartIdAndCourseId(Long cartId, Long courseId);

    void deleteByCartIdAndCourseId(Long cartId, Long courseId);
}
