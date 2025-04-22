package com.pcrt.Pcrt.repository;

import com.pcrt.Pcrt.entities.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Integer> {

    @Query("SELECT b FROM Blacklist b WHERE b.nationalId LIKE %:nationalId% or b.name LIKE %:name%")
    List<Blacklist> getAllBlacklistByChecking(@Param("nationalId") String nationalId, @Param("name") String name);
}
