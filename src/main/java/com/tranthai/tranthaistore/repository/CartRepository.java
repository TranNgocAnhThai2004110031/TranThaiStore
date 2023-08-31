package com.tranthai.tranthaistore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tranthai.tranthaistore.model.Cart;

public interface CartRepository  extends JpaRepository<Cart,Long>{
    Cart findByUser_id(Long id);
}
