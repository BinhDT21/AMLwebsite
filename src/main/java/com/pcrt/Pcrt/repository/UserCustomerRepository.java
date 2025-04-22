package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomerRepository extends JpaRepository<UserCustomer, Integer> {
}
