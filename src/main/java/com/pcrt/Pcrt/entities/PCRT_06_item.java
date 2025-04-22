package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pcrt_06_item")
public class PCRT_06_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal amount;
    private int frequency;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "pcrt_06_id")
    private PCRT_06 pcrt_06;


    public PCRT_06_item() {
    }

    public PCRT_06_item(int id, BigDecimal amount, int frequency, Customer customer, PCRT_06 pcrt_06) {
        this.id = id;
        this.amount = amount;
        this.frequency = frequency;
        this.customer = customer;
        this.pcrt_06 = pcrt_06;
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public PCRT_06 getPcrt_06() {
        return pcrt_06;
    }

    public void setPcrt_06(PCRT_06 pcrt_06) {
        this.pcrt_06 = pcrt_06;
    }
}
