package com.tranthai.tranthaistore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tranthai.tranthaistore.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
    
    List<Bill> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            String firstName, String lastName, String email, String phoneNumber);

}
