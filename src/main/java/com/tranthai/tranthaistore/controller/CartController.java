package com.tranthai.tranthaistore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.tranthai.tranthaistore.model.Cart;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CartService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;
import com.tranthai.tranthaistore.utils.CartUtil;
import com.tranthai.tranthaistore.utils.UserUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    private CartUtil cartUtil;

    @Autowired
    private UserUtil userHelper;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    private static Map<Long, Integer> cart;

    public static void clearCart() {
        cart = null;
    }

    public void mergeSessionCartWithUserCart(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Map<Long, Integer> sessionCart = (Map<Long, Integer>) session.getAttribute("cart");
    
        if (sessionCart != null && !"anonymousUser".equals(email)) {
            User user = this.userService.getUserByEmail(email);
            Cart userCart = this.cartService.getOrCreateCartForUser(user);
    
            Map<Long, Integer> userCartItem = userCart.getItems();
            userCartItem.putAll(sessionCart);
    
            this.cartService.addCart(userCart); 
    
            session.removeAttribute("cart");
        }
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
        String email = this.userHelper.getCurrentUsername();
        User user = this.userService.getUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    @PostMapping()
    public String addCart(@RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity, HttpSession session, Model model) {

        String email = (String) session.getAttribute("email");
        User user = null;
        Cart cartCurrent = null;
        Map<Long, Integer> item;

        if (email != null && !"anonymousUser".equals(email) ) {
            user = this.userService.getUserByEmail(email);
            cartCurrent = this.cartService.getOrCreateCartForUser(user);
            item = cartCurrent.getItems();
        } else {
            item = this.cartService.getOrCreateCartFromSession(session);
        }

        this.cartService.updateCartItemQuantity(item, productId, quantity, false);
        this.cartService.updateCartTotalsAndSession(session, item);

        if (user != null) {
            this.cartService.addCart(cartCurrent);
        }

        return "redirect:/shop";
    }

    @GetMapping()
    public String getCart(Model model, HttpSession session) {
        cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
            return "cart";
        }

        List<Product> products = this.productService.getAllProduct();
        
        for (Product product : products) {
            if (cart.containsKey(product.getId())) {
                model.addAttribute("quantity" + product.getId(), cart.get(product.getId()));
            }
        }
        this.cartUtil.handleCartUpdate(session, cart);
        this.cartService.updateCartTotalsAndSession(session, cart);
        model.addAttribute("products", products);
        model.addAttribute("categories", this.categoryService.getAllCategory());
        model.addAttribute("brands", this.brandService.getAllBrand());
        return "cart";
    }

    @GetMapping("/update/{id}")
    public String updateCart(@PathVariable("id") Long id,
        @RequestParam("quantity") int quantity, HttpSession session) {

        cart = (Map<Long, Integer>) session.getAttribute("cart");
        String email = (String) session.getAttribute("email");

        if (cart != null && cart.containsKey(id)) {
            this.cartService.updateCartItemQuantity(cart, id, quantity, true);
            this.cartService.updateCartTotalsAndSession(session, cart);
            if (email != null && !"anonymousUser".equals(email)) {
                User user = this.userService.getUserByEmail(email);
                Cart cartCurrent = this.cartService.getOrCreateCartForUser(user);
                cartCurrent.setItems(cart);
                this.cartService.addCart(cartCurrent);
            }
        }
        
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String deleteCart(@PathVariable("id") Long id, HttpSession session){
        cart = (Map<Long, Integer>) session.getAttribute("cart");
        String email = (String) session.getAttribute("email");

        if (cart != null && cart.containsKey(id)) {
            cart.remove(id);
            this.cartService.updateCartTotalsAndSession(session, cart);
            if (email != null && !"anonymousUser".equals(email)) {
                User user = this.userService.getUserByEmail(email);
                Cart cartCurrent = this.cartService.getOrCreateCartForUser(user);
                cartCurrent.setItems(cart);
                this.cartService.addCart(cartCurrent);
            }
        }

        return "redirect:/cart";
    }

}
