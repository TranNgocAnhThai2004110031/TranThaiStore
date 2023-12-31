package com.tranthai.tranthaistore.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tranthai.tranthaistore.converter.BillConverter;
import com.tranthai.tranthaistore.dto.BillDTO;
import com.tranthai.tranthaistore.model.Bill;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.model.User;
import com.tranthai.tranthaistore.service.BillService;
import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CartService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;
import com.tranthai.tranthaistore.utils.UserUtil;

@Controller
public class BillController {
    @Autowired 
    private BillService billService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BillConverter billConverter;

    @Autowired 
    private CategoryService categoryService;

    @Autowired 
    private CartService cartService;

    @Autowired 
    private UserService userService;

    @Autowired
    private UserUtil userUtil;

    @Autowired 
    private BrandService brandService;

    @GetMapping("/admin/bills/search")
    public String searchBill(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes){
        List<Bill> bills = this.billService.searchBill(keyword);
        redirectAttributes.addFlashAttribute("bills", bills);
        redirectAttributes.addFlashAttribute("keyword", keyword);
        return "redirect:/admin/bills";
    }

    @GetMapping("/admin/viewbill/{id}")
    public String viewAdminBill(@PathVariable("id") Long id, Model model) {
        BillDTO billDTO = this.billConverter.toDTO(this.billService.getBillById(id).get());
        List<Product> products = this.productService.getAllProduct();
        List<String> productName = billDTO.getProductName();
        this.billConverter.processProductInfo(productName, products, model, id);
        model.addAttribute("titlePage", "VIEW BILL, ADMIN");

        return "viewBillAdmin";
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session) {
        String email = this.userUtil.getCurrentUsername();
        if (email != null) {
            User user = this.userService.getUserByEmail(email);
            List<Bill> bills = this.billService.getBillByUserId(user.getId());
            model.addAttribute("bills", bills);
            model.addAttribute("categories", this.categoryService.getAllCategory());
            model.addAttribute("email", email);
            model.addAttribute("brands", this.brandService.getAllBrand());
        }
        
        return "history";
    }

    @GetMapping("/viewbill/{id}")
    public String vviewBill(@PathVariable("id") Long id, Model model) {
        BillDTO billDTO = this.billConverter.toDTO(this.billService.getBillById(id).get());
        List<Product> products = this.productService.getAllProduct();
        List<String> productName = billDTO.getProductName();
        this.billConverter.processProductInfo(productName, products, model, id);

        return "viewBill";
    }

    @GetMapping("/bill")
    public String getBill(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("billDTO", new BillDTO());
        if (email != null) {
            User user = this.userService.getUserByEmail(email);
            session.setAttribute("userId", user.getId());
            model.addAttribute("email", email);
        }
        session.setAttribute("cart", cart);

        return "checkout";
    }

    @PostMapping("/bill")
    public String addBill(@ModelAttribute("billDTO") BillDTO billDTO, Model model, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        Bill bill = new Bill();
        bill = this.billConverter.toEntity(billDTO);
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        List<String> productName = new ArrayList<>();
        List<Product> products = new ArrayList<Product>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product originalProduct = this.productService.getProductById(entry.getKey()).orElse(null);
            
            if (originalProduct != null) {
                Product productCopy = new Product(originalProduct); 
                
                productName.add(productCopy.getName() + " x " + entry.getValue());
                
                productCopy.setQuantity(originalProduct.getQuantity() - entry.getValue());
                this.productService.addProduct(productCopy); 
                productCopy.setQuantity(entry.getValue());
                products.add(productCopy); 
            }
        }

        double total = (double) session.getAttribute("total");

        bill.setProductName(productName);
        bill.setPrice(total);
        this.billService.addBill(bill);
        BillDTO billDTOCurrennt = this.billConverter.toDTO(bill);
        model.addAttribute("bill", billDTOCurrennt);
        model.addAttribute("productList", products);
        model.addAttribute("productName", productName);
        model.addAttribute("total", total);
        model.addAttribute("products", productService.getAllProduct());
        
        cart.clear();
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        model.addAttribute("email", email);
        session.setAttribute("userId", user.getId());
        session.setAttribute("cartCount", cart.size());
        Long cartId = this.cartService.getCartByUserId(user.getId()).getId();
        this.cartService.removeCartById(cartId);

        return "orderPlaced";
    }
}
