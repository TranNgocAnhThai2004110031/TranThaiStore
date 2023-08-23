package com.tranthai.tranthaistore.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranthai.tranthaistore.model.Cart;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.repository.CartRepository;
import com.tranthai.tranthaistore.service.CartService;
import com.tranthai.tranthaistore.service.ProductService;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired 
    private ProductService productService;

    @Override
    public List<Cart> getAllCart() {
        return this.cartRepository.findAll();
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        return this.cartRepository.findById(id);
    }

    @Override
    public void addCart(Cart cart) {
        this.cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long id) {
        return this.cartRepository.findByUser_id(id);
    }

    @Override
    public Cart getOrCreateCartForUser(User user) {
        Cart exitingCart = this.cartRepository.findByUser_id(user.getId());
        
        if (exitingCart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new HashMap<>());
            return this.cartRepository.save(newCart);
        }

        return exitingCart;
    }

    @Override
    public Map<Long, Integer> getOrCreateCartFromSession(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @Override
    public void updateCartItemQuantity(Map<Long, Integer> cart, Long productId, int quantity, boolean update) {
        if (cart.containsKey(productId)) {
            int currentQuantity = cart.get(productId);
            if (update) {
                currentQuantity = quantity;
            } else {
                currentQuantity += quantity;
            }
            cart.put(productId, currentQuantity);
        } else {
            cart.put(productId, quantity);
        }
    }

    @Override
    public void updateCartTotalsAndSession(HttpSession session, Map<Long, Integer> cart) {
        int count = 0;
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            count += entry.getValue();
            Product product = this.productService.getProductById(entry.getKey()).orElse(null);
            if (product != null) {
                total += product.getPrice() * entry.getValue();
            }
        }
        System.out.println(total);
        session.setAttribute("cart", cart);
        session.setAttribute("cartCount", count);
        session.setAttribute("total", total);
    }

    @Override
    public void removeCartById(Long id) {
        this.cartRepository.deleteById(id);
    }

    // @Override
    // public void updateCart(Cart cart) {
    //     this.cartRepository
    // }
    
}
