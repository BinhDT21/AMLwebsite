package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryQuery {

    @Autowired
    private EntityManager entityManager;

    public List<User> loadUsers (Map<String, String> params){
        String jpql = "SELECT u " +
                "FROM User u " +
                "WHERE 1=1 ";

        String role = params.get("role");
        boolean filterByRole = role != null && !role.isEmpty() && !role.equals("all");
        if(filterByRole){
            jpql += "AND u.role = :role ";
        }

        String username = params.get("username");
        boolean filterByUsername = username != null && !username.isEmpty();
        if(filterByUsername){
            jpql += "AND u.username = :username ";
        }


        Query query = entityManager.createQuery(jpql, User.class);

        if(filterByRole){
            query.setParameter("role", role);
        }
        if(filterByUsername){
            query.setParameter("username", username);
        }

        //pagination
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        int size = 10;

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countUser (Map<String, String> params){
        String jpql = "SELECT COUNT(u) FROM User u WHERE 1=1 ";

        String role = params.get("role");
        boolean filterByRole = role != null && !role.isEmpty() && !role.equals("all");
        if(filterByRole){
            jpql += "AND u.role = :role";
        }
        Query query = entityManager.createQuery(jpql);

        if(filterByRole){
            query.setParameter("role", role);
        }

        return (long)query.getSingleResult();
    }

    public List<User> findUsersExcludingId (int userId){
        String jpql = "SELECT u FROM User u WHERE 1=1 AND u.id != :userId ";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("userId", userId);

        return query.getResultList();
    }
}
