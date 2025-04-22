package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private User staff;
    @Column(name = "staff_evaluate")
    private String staffEvaluate;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
    @Column(name = "manager_evaluate")
    private String managerEvaluate;
    @Column(name = "created_date")
    private LocalDate createdDate;
    @Transient
    private String createdDateTmp;
    //pending is_used
    private String status;

    public Review(Customer customer, User staff, String staffEvaluate, User manager, String managerEvaluate, LocalDate createdDate) {
        this.customer = customer;
        this.staff = staff;
        this.staffEvaluate = staffEvaluate;
        this.manager = manager;
        this.managerEvaluate = managerEvaluate;
        this.createdDate = createdDate;
    }

    public Review(Customer customer, User staff, String staffEvaluate, LocalDate createdDate) {
        this.customer = customer;
        this.staff = staff;
        this.staffEvaluate = staffEvaluate;
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Review() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getStaff() {
        return staff;
    }

    public void setStaff(User staff) {
        this.staff = staff;
    }

    public String getStaffEvaluate() {
        return staffEvaluate;
    }

    public void setStaffEvaluate(String staffEvaluate) {
        this.staffEvaluate = staffEvaluate;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public String getManagerEvaluate() {
        return managerEvaluate;
    }

    public void setManagerEvaluate(String managerEvaluate) {
        this.managerEvaluate = managerEvaluate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public String getCreatedDateTmp() {
        return createdDateTmp;
    }

    public void setCreatedDateTmp(String createdDateTmp) {
        this.createdDateTmp = createdDateTmp;
    }

    @Override
    public String toString() {
        return "Review{" +
                "customer=" + customer +
                ", staff=" + staff +
                ", staffEvaluate='" + staffEvaluate + '\'' +
                ", manager=" + manager +
                ", managerEvaluate='" + managerEvaluate + '\'' +
                '}';
    }
}
