package com.tranthai.tranthaistore.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.tranthai.tranthaistore.dto.BillDTO;
import com.tranthai.tranthaistore.model.Bill;
import com.tranthai.tranthaistore.model.Product;
import com.tranthai.tranthaistore.service.BillService;
import com.tranthai.tranthaistore.service.UserService;

@Component
public class BillConverter {
    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    public Bill toEntity(BillDTO billDTO){
        Bill bill = new Bill();
        // bill.setId(billDTO.getId());
        bill.setFirstName(billDTO.getFirstName());
        bill.setLastName(billDTO.getLastName());
        bill.setAddress(billDTO.getAddress());
        bill.setTownCity(billDTO.getTownCity());
        bill.setPhoneNumber(billDTO.getPhoneNumber());;
        bill.setEmail(billDTO.getEmail());
        bill.setUser(this.userService.getUserById(billDTO.getUserId()).get());
        bill.setAddInformation(billDTO.getAddInformation());
        bill.setProductName(billDTO.getProductName());
        bill.setPrice(billDTO.getPrice());

        return bill;
    }

    public BillDTO toDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        billDTO.setId(bill.getId());
        billDTO.setFirstName(bill.getFirstName());
        billDTO.setLastName(bill.getLastName());
        billDTO.setAddress(bill.getAddress());
        billDTO.setTownCity(bill.getTownCity());
        billDTO.setPhoneNumber(bill.getPhoneNumber());;
        billDTO.setEmail(bill.getEmail());
        billDTO.setUserId(bill.getUser().getId());
        billDTO.setAddInformation(bill.getAddInformation());
        billDTO.setProductName(bill.getProductName());
        billDTO.setPrice(bill.getPrice());

        return billDTO;
    }

    public void processProductInfo(List<String> productName, List<Product> productCurrent, Model model, Long id) {
        BillDTO billDTO = toDTO(this.billService.getBillById(id).get());
        List<String> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        for (String subString : productName) {
            String[] parts = subString.trim().split(" x ");
            if (parts.length == 2) {
                products.add(parts[0].trim());
                quantities.add(Integer.parseInt(parts[1].trim()));
            }
        }

        Map<String, Double> productPriceMap = new HashMap<>();
        for (Product product : productCurrent) {
            productPriceMap.put(product.getName(), product.getPrice());
        }

        List<Double> prices = new ArrayList<>();
        for (String product : products) {
            Double productPrice = productPriceMap.get(product);
            if (productPrice != null) {
                int quantity = quantities.get(products.indexOf(product));
                prices.add(productPrice * quantity);
            }
        }

        
        model.addAttribute("billDTO", billDTO);
        model.addAttribute("products", products);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
    }
}
