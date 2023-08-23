package com.tranthai.tranthaistore.utils;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tranthai.tranthaistore.model.Cart;
import com.tranthai.tranthaistore.service.CartService;
import com.tranthai.tranthaistore.service.UserService;

@Component
public class CartUtil {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    public void handleCartUpdate(HttpSession session, Map<Long, Integer> cart) {
        String email = (String) session.getAttribute("email");
        this.cartService.getOrCreateCartFromSession(session);
        if (email != null) {   
            Cart cartCurrent = this.cartService.getOrCreateCartForUser(this.userService.getUserByEmail(email)); 
            Map<Long, Integer> currentItems = cartCurrent.getItems();
            Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
            if (cart != null && loggedIn != null && loggedIn) {
                for(Map.Entry<Long, Integer> entry : cart.entrySet()){
                    if (currentItems.containsKey(entry.getKey())) {
                        this.cartService.updateCartItemQuantity(currentItems, entry.getKey(), entry.getValue(), false);
                    } else {
                        this.cartService.updateCartItemQuantity(currentItems, entry.getKey(), entry.getValue(), true);
                    }
                }
            }
            this.cartService.updateCartTotalsAndSession(session, currentItems);
            cartCurrent.setItems(currentItems);
            this.cartService.addCart(cartCurrent);
            session.setAttribute("cart", currentItems);
            session.setAttribute("loggedIn", false);
        } 
    }
}
