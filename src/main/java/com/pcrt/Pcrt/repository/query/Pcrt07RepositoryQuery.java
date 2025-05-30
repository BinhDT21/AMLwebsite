package com.pcrt.Pcrt.repository.query;


import com.pcrt.Pcrt.entities.PCRT_06;
import com.pcrt.Pcrt.entities.PCRT_07;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class Pcrt07RepositoryQuery {
    @Autowired
    private EntityManager entityManager;

    public List<PCRT_07> getListPCRT_07 (Map<String, String> params){

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        int size = 10;

        String jpql = "SELECT pcrt07 " +
                "FROM PCRT_07 pcrt07";

        Query query = entityManager.createQuery(jpql, PCRT_07.class);

        query.setFirstResult(page * size);
        query.setMaxResults(size);


        return query.getResultList();
    }

    public long countPCRT_07 (){
        String jpql = "SELECT COUNT(pcrt07) FROM PCRT_07 pcrt07";

        Query query = entityManager.createQuery(jpql);
        return (long)query.getSingleResult();
    }
}
