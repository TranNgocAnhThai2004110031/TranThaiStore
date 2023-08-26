package com.tranthai.tranthaistore.converter;

import org.springframework.stereotype.Component;

import com.tranthai.tranthaistore.dto.BrandDTO;
import com.tranthai.tranthaistore.model.Brand;

@Component
public class BrandConverter {
    public Brand toENtity(BrandDTO brandDTO){
        Brand brand = new Brand();
        brand.setId(brandDTO.getId());
        brand.setName(brandDTO.getName());
        brand.setImageName(brandDTO.getImageName());
        return brand;
    }

    public BrandDTO toDTO(Brand brand){
        BrandDTO dto = new BrandDTO();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setImageName(brand.getImageName());
        return dto;
    }
}
