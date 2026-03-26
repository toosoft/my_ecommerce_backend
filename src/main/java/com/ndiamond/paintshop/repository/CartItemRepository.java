package com.ndiamond.paintshop.repository;

import com.ndiamond.paintshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.lang.ScopedValue;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
//    <T> ScopedValue<T> findByCartIdAndProductId(Long id, Long productId);
}
