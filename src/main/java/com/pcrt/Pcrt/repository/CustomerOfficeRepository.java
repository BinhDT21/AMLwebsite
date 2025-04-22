package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.CustomerOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOfficeRepository extends JpaRepository<CustomerOffice, Integer> {
}
