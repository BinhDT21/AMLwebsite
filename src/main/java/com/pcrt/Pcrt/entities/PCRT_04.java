package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pcrt_04")
public class PCRT_04 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "gdv_id")
    private User gdv;
    @ManyToOne
    @JoinColumn(name = "ksv_id")
    private User ksv;
    @JoinColumn(name = "manager_id")
    @ManyToOne
    private User manager;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Transient
    private String createdDateTmp;


    public PCRT_04() {
    }

    public PCRT_04(Customer customer, String filePath, LocalDateTime createdDate) {
        this.customer = customer;
        this.filePath = filePath;
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

    public User getGdv() {
        return gdv;
    }

    public void setGdv(User gdv) {
        this.gdv = gdv;
    }

    public User getKsv() {
        return ksv;
    }

    public void setKsv(User ksv) {
        this.ksv = ksv;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "PCRT_04{" +
                "filePath='" + filePath + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
