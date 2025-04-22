package com.pcrt.Pcrt.repository;


import com.pcrt.Pcrt.entities.PCRT_07_item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pcrt07ItemRepository extends JpaRepository<PCRT_07_item, Integer> {
}
