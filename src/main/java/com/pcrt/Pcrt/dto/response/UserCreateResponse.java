package com.pcrt.Pcrt.dto.response;

import com.pcrt.Pcrt.entities.User;

public class UserCreateResponse {
    private String result;
    private User user;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCreateResponse(String result, User user) {
        this.result = result;
        this.user = user;
    }
}
