package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.Kqks;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KqksRepositoryQuery {
    @Autowired
    private EntityManager entityManager;

    public List<Kqks> getListKqksByCustomer (int customerId){

        String jpql = "SELECT k FROM Kqks k WHERE k.customer.id = :customerId";

        Query query = entityManager.createQuery(jpql, Kqks.class);
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }
}
