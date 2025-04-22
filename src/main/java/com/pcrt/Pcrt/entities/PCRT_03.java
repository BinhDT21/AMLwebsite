package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pcrt_03")
public class PCRT_03 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Valid
    private Customer customer;
    @Column(name = "file_path")
    private String filePath;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "pcrt03")
    private List<PCRT_03_detail> details;
    @Transient
    private String createdDateTmp;

    public PCRT_03 (Customer customer, String path, User user, LocalDateTime localDateTime){
        this.customer = customer;
        this.filePath = path;
        this.user = user;
        this.createdDate = localDateTime;
    }

    public PCRT_03() {
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

    public String getCreatedDateTmp() {
        return createdDateTmp;
    }

    public void setCreatedDateTmp(String createdDateTmp) {
        this.createdDateTmp = createdDateTmp;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "PCRT_03{" +
                "filePath='" + filePath + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
