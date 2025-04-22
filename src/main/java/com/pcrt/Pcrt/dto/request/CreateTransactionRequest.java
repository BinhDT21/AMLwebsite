package com.pcrt.Pcrt.dto.request;

import java.math.BigDecimal;

public class CreateTransactionRequest {

    private int transactionId;
    private String amount;
    private String nationalId;

    public CreateTransactionRequest(String nationalId, String amount, int transactionId) {
        this.nationalId = nationalId;
        this.amount = amount;
        this.transactionId = transactionId;
    }

    public CreateTransactionRequest() {
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "CreateTransactionRequest{" +
                "amount='" + amount + '\'' +
                ", nationalId='" + nationalId + '\'' +
                '}';
    }
}
