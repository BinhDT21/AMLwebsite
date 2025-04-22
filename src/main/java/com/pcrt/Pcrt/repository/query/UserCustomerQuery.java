package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.UserCustomer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCustomerQuery {
    @Autowired
    private EntityManager entityManager;

    public List<UserCustomer> getListUserCustomerByCustomer (int customerId){

        String jpql = "SELECT uc FROM UserCustomer uc WHERE uc.customer.id = :customerId";

        Query query = entityManager.createQuery(jpql, UserCustomer.class);
        query.setParameter("customerId", customerId);

        return query.getResultList();
    }
}
