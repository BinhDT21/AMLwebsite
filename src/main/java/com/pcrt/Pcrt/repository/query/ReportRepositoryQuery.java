package com.pcrt.Pcrt.repository.query;

import com.pcrt.Pcrt.entities.Report;
import com.pcrt.Pcrt.entities.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReportRepositoryQuery {

    @Autowired
    private EntityManager entityManager;


    public List<Report> getReportList (Map<String, String> params){

        String createdDateParam = params.get("created-date");
        LocalDate createdDate = LocalDate.now();
        if(createdDateParam != null && !createdDateParam.isEmpty()){
            // Nếu có truyền param thì gán ngay trên param vào biến createdDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            createdDate = LocalDate.parse(createdDateParam, formatter);
        }

        // Định nghĩa khoảng thời gian từ 00:00:00 đến 23:59:59 của ngày cần lọc
        LocalDateTime startOfDay = createdDate.atStartOfDay();
        LocalDateTime endOfDay = createdDate.plusDays(1).atStartOfDay();

        String predicate = "";
        if(params.get("customer-id")!=null && !params.get("customer-id").isEmpty()){
            int customerId = Integer.parseInt(params.get("customer-id"));
            predicate += " and t.customer.id = " + customerId + " ";
        }

        String jpql = "select r " +
                "from Report r " +
                "where r.createdDate >= :startOfDay AND r.createdDate < :endOfDay " + predicate +
                "order by r.id desc";

        Query query = entityManager.createQuery(jpql, Report.class);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);

        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        int size = 10;

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countReport (Map<String, String> params){

        String jpql = "SELECT COUNT(r) FROM Report r WHERE 1=1 ";

        LocalDate createdDate = LocalDate.now();


        if(params.containsKey("created-date") && !params.get("created-date").isEmpty()){
            String createdDateStr = params.get("created-date");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            createdDate = LocalDate.parse(createdDateStr, formatter);
        }

        // Định nghĩa khoảng thời gian từ 00:00:00 đến 23:59:59 của ngày cần lọc
        LocalDateTime startOfDay = createdDate.atStartOfDay();
        LocalDateTime endOfDay = createdDate.plusDays(1).atStartOfDay();

        jpql += "AND r.createdDate >= :startOfDay AND r.createdDate < :endOfDay ";

        Query query = entityManager.createQuery(jpql);

        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);

        return (long)query.getSingleResult();

    }

}
