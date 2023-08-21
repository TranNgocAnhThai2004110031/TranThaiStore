package com.tranthai.tranthaistore.dto;

import java.util.List;

import lombok.Data;

@Data
public class BillDTO {
    private long id;
    private List<String> productName;
    private long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String town_city;
    private String phoneNumber;
    private String email;
    private String addInformation;
    private double price;
}
