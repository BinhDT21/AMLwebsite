package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.PCRT_04;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pcrt04Repository extends JpaRepository<PCRT_04, Integer> {
}
