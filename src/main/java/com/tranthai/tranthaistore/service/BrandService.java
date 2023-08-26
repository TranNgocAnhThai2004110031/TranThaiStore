package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;
import com.tranthai.tranthaistore.model.Brand;

public interface BrandService {
    List<Brand> getAllBrand();
    void addBrand(Brand brand);
    void removeBrandById(Long id);
    Optional<Brand> getBrandById(long id);
    List<Brand> searchBrand(String keyword);
}
