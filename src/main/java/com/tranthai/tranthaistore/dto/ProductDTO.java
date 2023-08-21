package com.tranthai.tranthaistore.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private double price;
    private String description;
    private int quantity;
    private String imageName;
}
