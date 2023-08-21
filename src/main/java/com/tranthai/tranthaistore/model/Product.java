package com.tranthai.tranthaistore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="category_id", referencedColumnName = "category_id")
    private Category category;
    @Column(columnDefinition = "DOUBLE(20,2)")
    private double price;
    @Column(length = 2000)
    private String description;
    private int quantity;
    @Column(name = "image_name")
    private String imageName;
    // @Column(name = "active")
    // private Boolean active;
}
