package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_customer")
public class UserCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    public UserCustomer() {
    }

    public UserCustomer(LocalDateTime createdDate, LocalDateTime updatedDate, User user, Customer customer) {

        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
        this.customer = customer;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "UserCustomer{" +
                "id=" + id +
                ", createdDate= " + createdDate +
                ", updatedDate= " + updatedDate +
                ", user= " + user.getId() +
                ", customer= " + customer.getId() +
                '}';
    }
}
