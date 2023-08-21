package com.tranthai.tranthaistore.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.tranthai.tranthaistore.converter.ProductConverter;
import com.tranthai.tranthaistore.model.Cart;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.CartService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;
import com.tranthai.tranthaistore.util.UserHelper;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private CartService cartService;

    // private static List<Product> cart;

    // public static void clearCart() {
    //     cart = null;
    // }

    private static Map<Long, Integer> cart;

    public static void clearCart(){
        cart = null;
    }

    @ModelAttribute("email")
    public String getCurrentEmail(HttpSession session) {
        String email = userHelper.getCurrentUsername();
        if (!"anonymousUser".equals(email)) {
            session.setAttribute("email", email);
        }
        return email;
    }

    @ModelAttribute("userId")
    public Long getCurrentUserId() {
        String email = userHelper.getCurrentUsername();
        User user = userService.getUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    @PostMapping()
    public String addCart(@RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity, HttpSession session, Model model) {
        // Product product = this.productService.getProductById(productId).get();
        String email = "";
        if (session.getAttribute("email") != null) {
            email = session.getAttribute("email").toString();
            User user = this.userService.getUserByEmail(email);
            Cart cart = this.cartService.getCartByUserId(user.getId());
            Map<Long, Integer> item;
            if (cart == null) {
                item = new HashMap<>();
                item.put(productId, quantity);
                cart = new Cart();
                cart.setUser(user);
                cart.setItems(item);
                // this.cartService.addCart(cart);
            } else {
                item = cart.getItems();
                if (item.containsKey(productId)) {
                    int currentQuantity = item.get(productId);
                    currentQuantity += quantity;
                    item.put(productId, currentQuantity);
                    // this.cartService.addCart(cart);
                } else{
                    item.put(productId, quantity);
                }
                // this.cartService.addCart(cart);
            }
            this.cartService.addCart(cart);
            int count = 0;
            double total = 0;
            for(Map.Entry<Long, Integer> entry : item.entrySet()){
                count += entry.getValue();
                Product product = this.productService.getProductById(entry.getKey()).get();
                total += product.getPrice() * entry.getValue();
            }
            session.setAttribute("cart", item);
            session.setAttribute("cartCount", count);
            session.setAttribute("total", total);
        } else {
            cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            } 
            
            if (cart.containsKey(productId)) {
                int currentQuantity = cart.get(productId);
                currentQuantity += quantity;
                cart.put(productId, currentQuantity);
            } else {
                cart.put(productId, quantity);
            }

            int count = 0;
            double total = 0;
            for (Map.Entry<Long, Integer> entry : cart.entrySet()){
                count += entry.getValue();
                Product product = this.productService.getProductById(entry.getKey()).get();
                total += product.getPrice() * entry.getValue();
            }

            System.out.println("+++++++++++++++++++++++++++++++cart: " + cart);

            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", count);
            session.setAttribute("total", total);
        }

        return "redirect:/shop";
    }

}
