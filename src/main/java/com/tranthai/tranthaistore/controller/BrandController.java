package com.tranthai.tranthaistore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tranthai.tranthaistore.converter.BrandConverter;
import com.tranthai.tranthaistore.dto.BrandDTO;
import com.tranthai.tranthaistore.model.Brand;
import com.tranthai.tranthaistore.service.BrandService;

@Controller
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandConverter brandConverter;

    @GetMapping("/admin/brand/add")
    public String getBrand(Model model){
        model.addAttribute("brandDTO", new BrandDTO());
        model.addAttribute("titlePage", "ADD BRAND, ADMIN");

        return "brandAdd";
    }

    @PostMapping("/admin/brand/add")
    public String addBrand(@ModelAttribute("BrandDTO") BrandDTO brandDTO){
        Brand brand = this.brandConverter.toENtity(brandDTO);
        this.brandService.addBrand(brand);

        return "redirect:/admin/brands";
    }

    @GetMapping("/admin/brand/{id}")
    public String getBrandByID(@PathVariable("id") Long id, Model model){
        Brand brand = this.brandService.getBrandById(id).get();
        BrandDTO brandDTO = this.brandConverter.toDTO(brand);
        model.addAttribute("brandDTO", brandDTO);
        model.addAttribute("titlePage", "UPDATE BRAND, ADMIN");

        return "brandAdd";
    }

    @GetMapping("/admin/brand/delete/{id}")
    public String deleteBrand(@PathVariable("id") Long id){
        this.brandService.removeBrandById(id);
        return "redirect:/admin/brands";
    }

    @GetMapping("/admin/brands/search")
    public String searchBrand(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("keyword", keyword);
        redirectAttributes.addFlashAttribute("brands", this.brandService.searchBrand(keyword));
        return "redirect:/admin/brands";
    }
}
