package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.tranthai.tranthaistore.model.Brand;

public interface BrandService {
    List<Brand> getAllBrand();
    Page<Brand> getAllBrandPage(Pageable pageable);
    void addBrand(Brand brand);
    void removeBrandById(Long id);
    Optional<Brand> getBrandById(long id);
    List<Brand> searchBrand(String keyword);   
    Page<Brand> searchBrandPage(@Param("keyword") String keyword, Pageable pageable);

}
