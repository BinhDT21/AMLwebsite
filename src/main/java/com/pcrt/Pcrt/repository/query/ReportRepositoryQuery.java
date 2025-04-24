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


    public List<Report> getReportList(Map<String, String> params) {

        String jpql = "SELECT r FROM Report r WHERE 1=1 ";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //filter by customer Id
        String customerId = params.get("customer-id");
        boolean filterByCustomerId = customerId != null && !customerId.isEmpty();
        if (filterByCustomerId) {
            jpql += "AND r.customer.id = :customerId ";
        }


        //filter by created Date
        LocalDateTime startDateFilter = params.get("start-date") != null ? LocalDate.parse(params.get("start-date"), formatter).atStartOfDay() : null;
        LocalDateTime endDateFilter = params.get("end-date") != null ? LocalDate.parse(params.get("end-date"), formatter).plusDays(1).atStartOfDay() : null;

        if (startDateFilter != null && endDateFilter == null) {
            jpql += "AND r.createdDate >= :startDateFilter ";
        }
        if (endDateFilter != null && startDateFilter == null) {
            jpql += "AND r.createdDate < :endDateFilter ";
        }
        if (startDateFilter != null && endDateFilter != null) {
            jpql += "AND r.createdDate >= :startDateFilter AND r.createdDate < :endDateFilter ";
        }


        jpql += " ORDER BY r.id DESC ";
        Query query = entityManager.createQuery(jpql, Report.class);


        //set param
        if (filterByCustomerId) {
            query.setParameter("customerId", customerId);
        }
        if (startDateFilter != null && endDateFilter == null) query.setParameter("startDateFilter", startDateFilter);
        if (endDateFilter != null && startDateFilter == null) query.setParameter("endDateFilter", endDateFilter);
        if (startDateFilter != null && endDateFilter != null) {
            query.setParameter("startDateFilter", startDateFilter);
            query.setParameter("endDateFilter", endDateFilter);
        }
        //set param

        int page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 0;
        int size = 10;

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public long countReport(Map<String, String> params) {

        String jpql = "SELECT COUNT(r) FROM Report r WHERE 1=1 ";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        LocalDateTime startDateFilter = params.get("start-date") != null ? LocalDate.parse(params.get("start-date"), formatter).atStartOfDay() : null;
        LocalDateTime endDateFilter = params.get("end-date") != null ? LocalDate.parse(params.get("end-date"), formatter).plusDays(1).atStartOfDay() : null;

        if (startDateFilter != null && endDateFilter == null) {
            jpql += "AND r.createdDate >= :startDateFilter ";
        }
        if (endDateFilter != null && startDateFilter == null) {
            jpql += "AND r.createdDate < :endDateFilter ";
        }
        if (startDateFilter != null && endDateFilter != null) {
            jpql += "AND r.createdDate >= :startDateFilter AND r.createdDate < :endDateFilter ";
        }

        Query query = entityManager.createQuery(jpql);

        if (startDateFilter != null && endDateFilter == null) query.setParameter("startDateFilter", startDateFilter);
        if (endDateFilter != null && startDateFilter == null) query.setParameter("endDateFilter", endDateFilter);
        if (startDateFilter != null && endDateFilter != null) {
            query.setParameter("startDateFilter", startDateFilter);
            query.setParameter("endDateFilter", endDateFilter);
        }

        return (long) query.getSingleResult();
    }

}
