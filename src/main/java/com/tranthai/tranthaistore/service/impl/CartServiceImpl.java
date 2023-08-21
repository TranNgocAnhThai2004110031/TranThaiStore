package com.tranthai.tranthaistore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranthai.tranthaistore.model.Cart;
import com.tranthai.tranthaistore.repository.CartRepository;
import com.tranthai.tranthaistore.service.CartService;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

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

    // @Override
    // public void updateCart(Cart cart) {
    //     this.cartRepository
    // }
    
}
