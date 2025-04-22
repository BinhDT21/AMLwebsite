package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.PCRT_03;
import com.pcrt.Pcrt.entities.PCRT_03_detail;
import com.pcrt.Pcrt.entities.PCRT_04_detail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Pcrt03RepositoryQuery {
    @Autowired
    private EntityManager entityManager;

    public List<PCRT_03> getListPCRT_03ByCustomerId (int customerId){
        String jpql = "SELECT p FROM PCRT_03 p WHERE p.customer.id = :customerId";

        Query query = entityManager.createQuery(jpql, PCRT_03.class);
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }

    // lấy pcrt03detail cuối của của customer
    public PCRT_03_detail getPCRT_03DetailByCustomerId (int customerId){
        String jpql = "select d from PCRT_03_detail d where d.pcrt03.customer.id = :customerId";

        Query query = entityManager.createQuery(jpql, PCRT_03_detail.class);
        query.setParameter("customerId", customerId);

        if(!query.getResultList().isEmpty())return (PCRT_03_detail)query.getResultList().getLast();
        else return null;
    }
}
