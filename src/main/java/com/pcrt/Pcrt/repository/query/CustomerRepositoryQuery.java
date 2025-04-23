package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomerRepositoryQuery {

    @Autowired
    private EntityManager entityManager;

    public List<Customer> getCustomers(Map<String, String> params) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> q = builder.createQuery(Customer.class);
        Root<Customer> r = q.from(Customer.class);
        q.select(r);

        ArrayList<Predicate> predicates = new ArrayList<>();

        //query name
        String name = params.get("name");
        if (name != null && !name.isEmpty()) {
            Predicate p = builder.like(r.get("name"), String.format("%%%s%%", name));
            predicates.add(p);
        }
        //query address
        String address = params.get("address");
        if (address != null && !address.isEmpty()) {
            Predicate p = builder.like(r.get("address"), String.format("%%%s%%", address));
            predicates.add(p);
        }

        //query status
        String status = params.get("status");
        if (status != null && !status.isEmpty() && !status.equals("all")) {
            Predicate p = builder.equal(r.get("status"), status);
            predicates.add(p);
        }
        //query type
        String type = params.get("type");
        if (type != null && !type.isEmpty() && !type.equals("all")) {
            Predicate p = builder.like(r.get("type"), String.format("%%%s%%", type));
            predicates.add(p);
        }
        //query riskClassification
        String riskClassification = params.get("riskClassification");
        if (riskClassification != null && !riskClassification.isEmpty() && !riskClassification.equals("all")) {
            predicates.add(builder.equal(r.get("riskClassification"), riskClassification));
        }
        //query city
        String city = params.get("city");
        if (city != null && !city.isEmpty()) {
            predicates.add(builder.like(r.get("city"), String.format("%%%s%%", city)));
        }
        //query country
        String country = params.get("country");
        if (country != null && !country.isEmpty()) {
            predicates.add(builder.like(r.get("country"), String.format("%%%s%%", country)));
        }

        //query passport
        String passport = params.get("passport");
        if (passport != null && !passport.isEmpty()) {
            predicates.add(builder.like(r.get("passport"), String.format("%%%s%%", passport)));
        }

        //query nationalid
        String nationalId = params.get("nationalId");
        if (nationalId != null && !nationalId.isEmpty()) {
            predicates.add(builder.like(r.get("nationalId"), String.format("%%%s%%", nationalId)));
        }
        q.where(predicates.toArray(Predicate[]::new));
        Query query = entityManager.createQuery(q);

        // Thêm phân trang
        int page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 0;
        int size = 10;
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countCustomer(Map<String, String> params) {
        String jpql = "SELECT COUNT(c) FROM Customer c WHERE 1=1 ";

        if (params.containsKey("status") && !params.get("status").isEmpty()) {
            if (!params.get("status").equals("all"))
                jpql += "AND c.status = :status ";
        }
        if (params.containsKey("type") && !params.get("type").isEmpty()) {
            if (!params.get("type").equals("all"))
                jpql += "AND c.type = :type ";
        }
        if (params.containsKey("riskClassification") && !params.get("riskClassification").isEmpty()) {
            if (!params.get("riskClassification").equals("all"))
                jpql += "AND c.riskClassification = :riskClassification ";
        }
        //name
        if (params.containsKey("name") && !params.get("name").isEmpty()) {
            jpql += "AND c.name LIKE :name ";
        }

        //nationalId
        if (params.containsKey("nationalId") && !params.get("nationalId").isEmpty()) {
            jpql += "AND c.nationalId LIKE :nationalId ";
        }

        //passport
        if (params.containsKey("passport") && !params.get("passport").isEmpty()) {
            jpql += "AND c.passport LIKE :passport ";
        }

        //address
        if (params.containsKey("address") && !params.get("address").isEmpty()) {
            jpql += "AND c.address LIKE :address ";
        }

        //city
        if (params.containsKey("city") && !params.get("city").isEmpty()) {
            jpql += "AND c.city LIKE :city ";
        }

        //country
        if (params.containsKey("country") && !params.get("country").isEmpty()) {
            jpql += "AND c.country LIKE :country ";
        }

        Query query = entityManager.createQuery(jpql);

        if (params.containsKey("status") && !params.get("status").isEmpty()) {
            if (!params.get("status").equals("all"))
                query.setParameter("status", params.get("status"));
        }
        if (params.containsKey("type") && !params.get("type").isEmpty()) {
            if (!params.get("type").equals("all"))
                query.setParameter("type", params.get("type"));
        }
        if (params.containsKey("riskClassification") && !params.get("riskClassification").isEmpty()) {
            if (!params.get("riskClassification").equals("all"))
                query.setParameter("riskClassification", params.get("riskClassification"));
        }
        if (params.containsKey("name") && !params.get("name").isEmpty()) {
            query.setParameter("name", "%" + params.get("name") + "%");
        }
        if (params.containsKey("nationalId") && !params.get("nationalId").isEmpty()) {
            query.setParameter("nationalId", "%" + params.get("nationalId") + "%");
        }
        if (params.containsKey("passport") && !params.get("passport").isEmpty()) {
            query.setParameter("passport", "%" + params.get("passport") + "%");
        }
        if (params.containsKey("address") && !params.get("address").isEmpty()) {
            query.setParameter("address", "%" + params.get("address") + "%");
        }
        if (params.containsKey("city") && !params.get("city").isEmpty()) {
            query.setParameter("city", "%" + params.get("city") + "%");
        }
        if (params.containsKey("country") && !params.get("country").isEmpty()) {
            query.setParameter("country", "%" + params.get("country") + "%");
        }



        return (long) query.getSingleResult();
    }

    public Set<User> getUsersByCustomer(int customerId) {
        String jpql = "SELECT uc.user FROM UserCustomer uc WHERE uc.customer.id=:customerId";
        Query query = entityManager.createQuery(jpql, User.class);
        query.setParameter("customerId", customerId);

        return new HashSet<>(query.getResultList());
    }

    public Optional<Customer> getCustomerByNationalId(String nationalId) {
        String jpql = "select c from Customer c where c.nationalId = :nationalId";

        Query query = entityManager.createQuery(jpql, Customer.class);
        query.setParameter("nationalId", nationalId);
        return query.getResultList().stream().findFirst();
    }

    public Optional<Customer> getCustomerByPassport(String passport) {
        String jpql = "select c from Customer c where c.passport = :passport";

        Query query = entityManager.createQuery(jpql, Customer.class);
        query.setParameter("passport", passport);
        return query.getResultList().stream().findFirst();
    }




}
