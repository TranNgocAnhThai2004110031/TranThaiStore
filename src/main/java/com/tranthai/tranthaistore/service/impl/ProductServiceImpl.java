package com.tranthai.tranthaistore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.repository.ProductRepository;
import com.tranthai.tranthaistore.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    @Override
    public void addProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public void removeProductById(Long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getProductById(long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProductByCategory(long id) {
        return this.productRepository.findAllByCategory_Id(id);   
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        return this.productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public Page<Product> getAllProductPage(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }
    
}
