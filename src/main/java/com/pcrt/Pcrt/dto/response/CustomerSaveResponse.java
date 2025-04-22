package com.pcrt.Pcrt.dto.response;

import com.pcrt.Pcrt.entities.Customer;

public class CustomerSaveResponse {
    private String result;
    private Customer customer;

    public CustomerSaveResponse(String resultMessage, Customer customer) {
        this.result = resultMessage;
        this.customer = customer;
    }

    public String getResultMessage() {
        return result;
    }

    public void setResultMessage(String resultMessage) {
        this.result = resultMessage;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
