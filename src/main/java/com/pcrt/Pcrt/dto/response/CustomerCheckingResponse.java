package com.pcrt.Pcrt.dto.response;

import com.pcrt.Pcrt.entities.Blacklist;

import java.util.List;

public class CustomerCheckingResponse {

    private String result;
    private List<Blacklist> blacklists;

    public CustomerCheckingResponse(String result, List<Blacklist> blacklists) {
        this.result = result;
        this.blacklists = blacklists;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Blacklist> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(List<Blacklist> blacklists) {
        this.blacklists = blacklists;
    }

    public CustomerCheckingResponse() {
    }

    @Override
    public String toString() {
        return "CustomerCheckingResponse: " + this.getResult() + " " + this.getBlacklists();
    }
}
