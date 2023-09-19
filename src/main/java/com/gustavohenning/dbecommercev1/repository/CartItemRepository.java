package com.gustavohenning.dbecommercev1.repository;

import com.gustavohenning.dbecommercev1.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
