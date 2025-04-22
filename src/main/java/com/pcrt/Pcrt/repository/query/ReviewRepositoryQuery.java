package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepositoryQuery {
    @Autowired
    private EntityManager entityManager;

    public List<Review> reviewList() {
        String jpql = "SELECT r FROM Review r " +
                "WHERE r.createdDate = CURRENT_DATE and r.manager is null";

        Query query = entityManager.createQuery(jpql, Review.class);
        return query.getResultList();
    }

    // get list review: review.managerEvaluate == 'pass' and review.status = "pending"
    public List<Review> reviewList(String evaluate) {
        String jpql = "SELECT r " +
                "FROM Review r " +
                "WHERE r.createdDate = CURRENT_DATE " +
                "and r.managerEvaluate = :evaluate and r.status ='pending' ";

        Query query = entityManager.createQuery(jpql, Review.class);
        query.setParameter("evaluate", evaluate);
        return query.getResultList();
    }


}
