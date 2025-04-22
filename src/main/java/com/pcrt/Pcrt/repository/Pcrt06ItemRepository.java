package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.PCRT_06_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pcrt06ItemRepository extends JpaRepository<PCRT_06_item, Integer> {
}
