package com.tranthai.tranthaistore.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tranthai.tranthaistore.converter.BillConverter;
import com.tranthai.tranthaistore.dto.BillDTO;
import com.tranthai.tranthaistore.model.Bill;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.service.BillService;
import com.tranthai.tranthaistore.service.CategoryService;
import com.tranthai.tranthaistore.service.ProductService;

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
}
