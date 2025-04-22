package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.PCRT_04_detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pcrt04DetailRepository extends JpaRepository<PCRT_04_detail, Integer> {
}
