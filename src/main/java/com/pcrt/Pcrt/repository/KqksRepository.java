package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.Kqks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KqksRepository extends JpaRepository<Kqks, Integer> {
}
