package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kqks")
public class Kqks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "file_path")
    private String filePath;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JoinColumn(name = "created_date")
    private LocalDateTime createdDate;
    @Transient
    private String createdDateTmp;

    public Kqks() {
    }

    public Kqks(Customer customer, String filePath, User user, LocalDateTime createdDate) {
        this.customer = customer;
        this.filePath = filePath;
        this.user = user;
        this.createdDate = createdDate;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
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
        return "Kqks{" +
                "filePath='" + filePath + '\'' +
                '}';
    }
}
