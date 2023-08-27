package com.tranthai.tranthaistore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tranthai.tranthaistore.model.Bill;

public interface BillService {
    List<Bill> getAllBill();

    Page<Bill> getAllBillPage(Pageable pageable);

    void addBill(Bill bill);

    void removeBill(Bill bill);

    Optional<Bill> getBillById(Long id);

    List<Bill> getBillByUserId(Long id);

    List<Bill> searchBill(String keyword);

    Page<Bill> searchBillPgae(String keyword, Pageable pageable);

}
