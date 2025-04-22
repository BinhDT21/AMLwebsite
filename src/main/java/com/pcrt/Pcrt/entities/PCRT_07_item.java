package com.pcrt.Pcrt.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pcrt_07_item")
public class PCRT_07_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal amount;
    private int frequency;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "pcrt_07_id")
    private PCRT_07 pcrt_07;

    public PCRT_07_item() {
    }

    public PCRT_07_item(int id, BigDecimal amount, int frequency, Customer customer, PCRT_07 pcrt_07) {
        this.id = id;
        this.amount = amount;
        this.frequency = frequency;
        this.customer = customer;
        this.pcrt_07 = pcrt_07;
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

    public PCRT_07 getPcrt_07() {
        return pcrt_07;
    }

    public void setPcrt_07(PCRT_07 pcrt_07) {
        this.pcrt_07 = pcrt_07;
    }
}
