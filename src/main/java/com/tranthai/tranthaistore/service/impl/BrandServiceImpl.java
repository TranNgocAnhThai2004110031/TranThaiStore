package com.tranthai.tranthaistore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranthai.tranthaistore.model.Brand;
import com.tranthai.tranthaistore.repository.BrandRepository;
import com.tranthai.tranthaistore.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
    @Autowired 
    private BrandRepository brandRepository;

    @Override
    public List<Brand> getAllBrand() {
        return this.brandRepository.findAll();
    }

    @Override
    public void addBrand(Brand brand) {
        this.brandRepository.save(brand);
    }

    @Override
    public void removeBrandById(Long id) {
        this.brandRepository.deleteById(id);
    }

    @Override
    public Optional<Brand> getBrandById(long id) {
        return this.brandRepository.findById(id);
    }

    @Override
    public List<Brand> searchBrand(String keyword) {
        return this.brandRepository.findByNameContainingIgnoreCase(keyword);
    }
    
}
