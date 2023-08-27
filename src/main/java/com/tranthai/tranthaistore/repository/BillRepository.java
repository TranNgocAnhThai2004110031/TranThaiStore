package com.tranthai.tranthaistore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tranthai.tranthaistore.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
    
    List<Bill> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(
            String firstName, String lastName, String email, String phoneNumber);

    List<Bill> findByUser_id(Long id);

    @Query("SELECT b FROM Bill b WHERE LOWER(b.firstName) LIKE %:keyword% OR LOWER(b.lastName) LIKE %:keyword% OR LOWER(b.email) LIKE %:keyword% OR LOWER(b.phoneNumber) LIKE %:keyword% OR LOWER(b.townCity) LIKE %:keyword% OR LOWER(b.address) LIKE %:keyword%")
    Page<Bill> searchBills(@Param("keyword") String keyword, Pageable pageable);


}
