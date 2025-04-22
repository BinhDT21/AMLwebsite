package com.pcrt.Pcrt.dto.request;

public class CreateReviewRequest {

    private int customerId;
    private String staffEvaluate;

    public CreateReviewRequest(int customerId, String staffEvaluate) {
        this.customerId = customerId;
        this.staffEvaluate = staffEvaluate;
    }

    public CreateReviewRequest() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }



    public String getStaffEvaluate() {
        return staffEvaluate;
    }

    public void setStaffEvaluate(String staffEvaluate) {
        this.staffEvaluate = staffEvaluate;
    }

    @Override
    public String toString() {
        return "CreateReviewRequest{" +
                "customerId=" + customerId +
                ", staffEvaluate=" + staffEvaluate +
                '}';
    }
}
