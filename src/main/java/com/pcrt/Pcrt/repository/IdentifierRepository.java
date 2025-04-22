package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.Identifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentifierRepository extends JpaRepository<Identifier, Integer> {
}
