package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.PCRT_04;
import com.pcrt.Pcrt.entities.PCRT_04_detail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Pcrt04RepositoryQuery {
    @Autowired
    private EntityManager entityManager;

    public List<PCRT_04> getListPCRT_04ByCustomerId (int customerId){
        String jpql = "SELECT p FROM PCRT_04 p WHERE p.customer.id = :customerId";

        Query query = entityManager.createQuery(jpql, PCRT_04.class);
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }


    // lấy pcrt04detail cuối của của customer
    public PCRT_04_detail getPCRT_04DetailByCustomerId (int customerId){
        String jpql = "SELECT d FROM PCRT_04_detail d WHERE d.pcrt04.customer.id = :customerId";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("customerId", customerId);

        List<PCRT_04_detail> detailList = query.getResultList();

        if(!detailList.isEmpty())return detailList.getLast();
        else return null;
    }
}
