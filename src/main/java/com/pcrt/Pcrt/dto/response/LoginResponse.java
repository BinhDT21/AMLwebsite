package com.pcrt.Pcrt.dto.response;

public class LoginResponse {
    private boolean authenticate;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(boolean authenticate, String token) {
        this.authenticate = authenticate;
        this.token = token;
    }

    public boolean isAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(boolean authenticate) {
        this.authenticate = authenticate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
