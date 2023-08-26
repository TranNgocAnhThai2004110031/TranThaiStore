package com.tranthai.tranthaistore.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tranthai.tranthaistore.dto.ProductDTO;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.service.BrandService;
import com.tranthai.tranthaistore.service.CategoryService;

@Component
public class ProductConverter {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    public Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(this.categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setBrand(this.brandService.getBrandById(productDTO.getBrandId()).get());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());
        product.setImageName(productDTO.getImageName());
        return product;
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setBrandId(product.getBrand().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());
        return productDTO;
    }
}
