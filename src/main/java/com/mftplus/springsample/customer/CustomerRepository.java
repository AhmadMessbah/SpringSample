package com.mftplus.springsample.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAll(Pageable pageable);
    Page<Customer> findByOrderByFirstNameAsc(Pageable pageable);
    Page<Customer> findByOrderByLastNameAsc(Pageable pageable);
}