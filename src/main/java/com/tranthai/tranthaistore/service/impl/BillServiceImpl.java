package com.tranthai.tranthaistore.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranthai.tranthaistore.model.Bill;
import com.tranthai.tranthaistore.repository.BillRepository;
import com.tranthai.tranthaistore.service.BillService;

@Service
public class BillServiceImpl implements BillService{

    @Autowired
    private BillRepository billRepository;

    @Override
    public List<Bill> getAllBill() {
        return this.billRepository.findAll();
    }

    @Override
    public void addBill(Bill bill) {
        this.billRepository.save(bill);
    }

    @Override
    public void removeBill(Bill bill) {
        this.billRepository.delete(bill);
    }

    @Override
    public Optional<Bill> getBillById(Long id) {
        return this.billRepository.findById(id);
    }

    @Override
    public List<Bill> searchBill(String keyword) {
        return this.billRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
                        keyword, keyword, keyword, keyword);
    }

    @Override
    public List<Bill> getBillByUserId(Long id) {
        return this.billRepository.findByUser_id(id);
    }
     
}
