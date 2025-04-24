package com.pcrt.Pcrt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateTransactionRequest {

    private int transactionId;
    @NotBlank(message = "Vui lòng nhập số tiền giao dịch")
    @NotNull(message = "Vui lòng nhập số tiền giao dịch")
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
