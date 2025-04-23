package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.Branch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BranchRepositoryQuery {

    @Autowired
    private EntityManager entityManager;

    public List<Branch> loadBranches (Map<String, String> params){
        String jpql = "SELECT b FROM Branch b WHERE 1=1 ";

        Query query = entityManager.createQuery(jpql, Branch.class);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        int size = 10;

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countBranch (Map<String, String> params){
        String jpql = "SELECT COUNT(b) FROM Branch b WHERE 1=1 ";
        Query query = entityManager.createQuery(jpql);

        return (long)query.getSingleResult();
    }
}
