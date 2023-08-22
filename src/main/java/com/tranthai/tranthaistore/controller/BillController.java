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
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;
import com.tranthai.tranthaistore.service.UserService;

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
    private UserService userService;

    @GetMapping("/admin/bills/search")
    public String searchBill(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes){
        List<Bill> bills = this.billService.searchBill(keyword);
        redirectAttributes.addFlashAttribute("bills", bills);
        return "redirect:/admin/bills";
    }

    @GetMapping("/admin/viewbill/{id}")
    public String viewAdminBill(@PathVariable("id") Long id, Model model) {
        BillDTO billDTO = this.billConverter.toDTO(this.billService.getBillById(id).get());
        List<Product> products = this.productService.getAllProduct();
        List<String> productName = billDTO.getProductName();
        this.billConverter.processProductInfo(productName, products, model, id);

        return "viewBillAdmin";
    }

    @GetMapping("/history/{id}")
    public String history(@PathVariable("id") Long id, Model model){
        List<Bill> bills = this.billService.getAllBill();
        List<Bill> billCurrents = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getUser().getId() == id) {
                billCurrents.add(bill);
            }
        }
        model.addAttribute("bills", billCurrents);
        model.addAttribute("categories", this.categoryService.getAllCategory());
        
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
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("billDTO", new BillDTO());
        cart.clear();
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        session.setAttribute("userId", user.getId());

        return "checkout";
    }

    @PostMapping("/bill")
    public String addBill(@ModelAttribute("billDTO") BillDTO billDTO, HttpSession session, Model model) {
        Bill bill = this.billConverter.toEntity(billDTO);
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        List<String> productName = new ArrayList<>();
        List<Product> products = new ArrayList<Product>();
        for(Map.Entry<Long, Integer> entry : cart.entrySet()){
            Product product = this.productService.getProductById(entry.getKey()).get();
            productName.add(product.getName() + " x " + entry.getValue());
            product.setQuantity(product.getQuantity() - entry.getValue());
            this.productService.addProduct(product);
        }

        double total = (double) session.getAttribute("total");

        bill.setProductName(productName);
        bill.setPrice(total);
        this.billService.addBill(bill);
        model.addAttribute("bill", bill);
        model.addAttribute("result", bill.getId());
        model.addAttribute("productList", products);
        model.addAttribute("productName", productName);
        model.addAttribute("total", total);
        model.addAttribute("products", productService.getAllProduct());

        cart.clear();
        String email = (String) session.getAttribute("email");
        User user = this.userService.getUserByEmail(email);
        session.setAttribute("userId", user.getId());
        session.setAttribute("cartCount", cart.size());

        return "orderPlaced";
    }
}
