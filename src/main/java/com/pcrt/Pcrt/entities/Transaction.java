package com.pcrt.Pcrt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "[transaction]")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(precision = 18, scale = 2)
    private BigDecimal amount;
    @Column(name = "created_date")
    private LocalDate createdDate;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "transaction")
    @JsonIgnore
    private List<Report> report;

    //pending or staff_checked
    //1. Giao dịch lần đầu được ktv khởi tạo
    //1.1 nếu có dấu hiệu đáng ngờ: risk
    //1.2 nếu không có dấu hiệu đáng ngờ: pending
    //2. Giám đốc chi nhánh kiểm tra các giao dịch: Risk
    //2.1 nếu đúng là đáng ngờ: Risk -> Report
    //2.2 nếu không đúng: Risk -> Pass
    //3. Bộ phận AML: lập các báo cáo giao dịch Report
    //3.1 Các giao dịch bị lập báo cáo sẽ được chuyển thành archived
    private String status;
    @Transient
    private String createdDateTmp;
    @Transient
    private String amountTmp;


    public Transaction() {
    }

    public Transaction(int id, BigDecimal amount, LocalDate createdDate, Customer customer, User user) {
        this.id = id;
        this.amount = amount;
        this.createdDate = createdDate;
        this.customer = customer;
        this.user = user;
    }

    public Transaction(BigDecimal amount, LocalDate createdDate, Customer customer, User user) {
        this.amount = amount;
        this.createdDate = createdDate;
        this.customer = customer;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getCreatedDateTmp() {
        return createdDateTmp;
    }

    public void setCreatedDateTmp(String createdDateTmp) {
        this.createdDateTmp = createdDateTmp;
    }

    public String getAmountTmp() {
        return amountTmp;
    }

    public void setAmountTmp(String amountTmp) {
        this.amountTmp = amountTmp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", createdDate=" + createdDate +
                ", status='" + status + '\'' +
                ", branch_id='" + branch.getId() + '\'' +
                ", customer_id='" + customer.getId() + '\'' +
                '}';
    }
}
