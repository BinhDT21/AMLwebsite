package com.pcrt.Pcrt.dto.response;

import com.pcrt.Pcrt.entities.Customer;

public class ReviewRecordsByNumberTransactionResponse {
    private Customer customer;
    private Long numberTransaction;

    public ReviewRecordsByNumberTransactionResponse() {
    }

    public ReviewRecordsByNumberTransactionResponse(Customer customer, Long numberTransaction) {
        this.customer = customer;
        this.numberTransaction = numberTransaction;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getNumberTransaction() {
        return numberTransaction;
    }

    public void setNumberTransaction(Long numberTransaction) {
        this.numberTransaction = numberTransaction;
    }


}
