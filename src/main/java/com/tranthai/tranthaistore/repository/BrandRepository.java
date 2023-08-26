package com.tranthai.tranthaistore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tranthai.tranthaistore.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>{
    List<Brand> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT b FROM Brand b WHERE LOWER(b.name) LIKE %:keyword%")
    Page<Brand> searchBrands(@Param("keyword") String keyword, Pageable pageable);

}
