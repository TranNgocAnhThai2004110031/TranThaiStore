package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;
import com.tranthai.tranthaistore.model.Bill;

public interface BillService {
    List<Bill> getAllBill();

    void addBill(Bill bill);

    void removeBill(Bill bill);

    Optional<Bill> getBillById(Long id);

    List<Bill> searchBill(String keyword);
}
