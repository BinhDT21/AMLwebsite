package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepositoryQuery {
    @Autowired
    private EntityManager entityManager;

    public List<Transaction> reviewRecordsByAmount(Map<String, String> params) {

        String jpql = "SELECT t " +
                "FROM Transaction t " +
                "WHERE t.customer.status = 'saved' " +
                "   AND t.amount >= 400000000 " +
                "   AND t.createdDate = CURRENT_DATE";

        String status = params.get("status");
        if (status != null && !status.isEmpty()) {
            jpql += " AND t.status = :status";
        }

        Query query = entityManager.createQuery(jpql, Transaction.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<Object[]> reviewRecordsByNumberTransaction(Map<String, String> params) {
        String jpql = "SELECT t.customer, COUNT(t)" +
                "FROM Transaction t " +
                "WHERE t.customer.status = 'saved' AND t.createdDate = CURRENT_DATE ";

        String status = params.get("status");
        if (status != null && !status.isEmpty()) {
            jpql += "AND t.status = :status ";
        }
        jpql += "GROUP BY  t.customer HAVING COUNT(t) >=20";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("status", status);

        return query.getResultList();
    }

    // lấy danh sách Transaction của mỗi Customer.nationalId
    public List<Transaction> getTransactionListByNationalId(String nationalId) {

        String jpql = "select t from Transaction t where t.customer.nationalId = :nationalId " +
                "and t.createdDate = CURRENT_DATE";
        Query query = entityManager.createQuery(jpql, Transaction.class);
        query.setParameter("nationalId", nationalId);

        return query.getResultList();
    }


    //lấy danh sách transaction theo customer Id trong ngày
    public List<Transaction> getTransactionListByCustomer (int customerId){
        String jpql = "select t from Transaction t where t.customer.id = :customerId and t.createdDate = CURRENT_DATE";
        Query query = entityManager.createQuery(jpql, Transaction.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    public List<Transaction> getTransactionList (Map<String, String> params){

        String predicate = " ";
        String status = params.get("status");
        if(status != null && !status.isEmpty()){
            switch (status){
                case "risk":
                    predicate+= "t.status = 'risk' ";break;
                case "pass":
                    predicate += "t.status = 'pass' ";break;
                case "report":
                    predicate += "t.status = 'report' ";break;
                case "archived":
                    predicate += "t.status = 'archived' ";break;
                default:
                    predicate += "(t.status = 'pending' or t.status = 'staff_checked') ";break;
            }
        }else predicate+=" (t.status = 'pending' or t.status = 'staff_checked') ";


        if(params.get("customer-id")!=null && !params.get("customer-id").isEmpty()){
            int customerId = Integer.parseInt(params.get("customer-id"));
            predicate += " and t.customer.id = " + customerId + " ";
        }

        String createdDateParam = params.get("created-date");
        LocalDate createdDate = null;
        if(createdDateParam != null && !createdDateParam.isEmpty()){
            // Nếu có truyền param thì gán ngay trên param vào biến createdDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            createdDate = LocalDate.parse(createdDateParam, formatter);
        }
        if(createdDate != null){
            predicate += " and t.createdDate = :createdDate ";
        }

        String jpql = "select t " +
                    "from Transaction t " +
                    "where " + predicate +
                    "order by t.id desc";


        Query query = entityManager.createQuery(jpql, Transaction.class);

        if(createdDate != null){
            query.setParameter("createdDate", createdDate);
        }

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        int size = 20;
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countTransaction (Map<String,String> params){
        String jpql = "SELECT COUNT(t) FROM Transaction t WHERE 1=1 ";

        String status = params.get("status");
        boolean filterByStatus = status!=null && !params.get("status").isEmpty();
        if(filterByStatus){
            if(!status.equals("finish")){
                jpql+= "AND t.status = :status ";
            }else
            {
                jpql += "AND (t.status = 'pending' or t.status = 'staff_checked') ";
            }
        }


        LocalDate createdDateParam = params.get("created-date") != null ? LocalDate.parse(params.get("created-date")): null;
        boolean filterByCreatedDate = createdDateParam!=null;
        if(filterByCreatedDate){

            jpql += "AND t.createdDate = :createdDateParam ";
        }


        Query query = entityManager.createQuery(jpql);

        if(filterByStatus){
            if(!status.equals("finish")){
               query.setParameter("status",status);
            }
        }

        if(filterByCreatedDate)query.setParameter("createdDateParam", createdDateParam);

        System.out.println("jpql: " + jpql);
        System.out.println("count: " + (long)query.getSingleResult());
        return (long)query.getSingleResult();
    }
}
