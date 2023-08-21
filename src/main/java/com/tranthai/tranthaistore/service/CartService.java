package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;

import com.tranthai.tranthaistore.model.Cart;

public interface CartService {
    List<Cart> getAllCart();
    Optional<Cart> getCartById(Long id);
    Cart getCartByUserId(Long id);
    void addCart(Cart cart);
    // void removeCart(Cart cart);
    // void updateCart(Cart cart);
}
