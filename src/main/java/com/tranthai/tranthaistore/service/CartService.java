package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.tranthai.tranthaistore.model.Cart;
import com.tranthai.tranthaistore.model.User;

public interface CartService {
    List<Cart> getAllCart();
    Optional<Cart> getCartById(Long id);
    Cart getCartByUserId(Long id);
    void addCart(Cart cart);
    Cart getOrCreateCartForUser(User user);
    Map<Long, Integer> getOrCreateCartFromSession(HttpSession session);
    void updateCartItemQuantity(Map<Long, Integer> cart, Long productId, int quantity, boolean update);
    void updateCartTotalsAndSession(HttpSession session, Map<Long, Integer> cart);
    void removeCartById(Long id);
    // void updateCart(Cart cart);
}
