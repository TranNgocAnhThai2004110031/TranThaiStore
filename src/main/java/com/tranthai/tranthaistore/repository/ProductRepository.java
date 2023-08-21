package com.tranthai.tranthaistore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tranthai.tranthaistore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategory_Id(long id);

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
