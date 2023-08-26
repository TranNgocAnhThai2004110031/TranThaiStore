package com.tranthai.tranthaistore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tranthai.tranthaistore.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>{
    List<Brand> findByNameContainingIgnoreCase(String name);
}
