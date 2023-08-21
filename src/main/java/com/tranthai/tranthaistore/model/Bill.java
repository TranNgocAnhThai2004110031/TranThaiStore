package com.tranthai.tranthaistore.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tranthai.tranthaistore.converter.StringListConverter;

import lombok.Data;

@Entity
@Data
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "product_name", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> productName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    @Column(name = "town_city")
    private String townCity;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String email;
    @Column(name = "add_information")
    private String addInformation;
    private double price;
}
