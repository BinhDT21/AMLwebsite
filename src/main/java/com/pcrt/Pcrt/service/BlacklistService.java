package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.dto.request.CustomerCheckingRequest;
import com.pcrt.Pcrt.dto.response.CustomerCheckingResponse;
import com.pcrt.Pcrt.entities.Blacklist;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlacklistService {

    @Autowired
    private BlacklistRepository blacklistRepository;

    public List<Blacklist> getBlacklists (){
        return this.blacklistRepository.findAll();
    }

    public CustomerCheckingResponse getBlacklistByCustomer(CustomerCheckingRequest request){
        CustomerCheckingResponse response = new CustomerCheckingResponse();
        List<Blacklist> blacklists = blacklistRepository
                .getAllBlacklistByChecking(request.getNationalId(), request.getName());

        if(!blacklists.isEmpty())
            response.setResult("Cảnh báo có " + blacklists.size() + " kết quả trùng khớp");
        else
            response.setResult("Không có kết quả trùng khớp");

        response.setBlacklists(blacklists);

        return response;
    }

    public CustomerCheckingResponse getBlacklistByCustomer (Customer customer){
        CustomerCheckingResponse response = new CustomerCheckingResponse();
        List<Blacklist> blacklists =blacklistRepository.getAllBlacklistByChecking(customer.getNationalId(), customer.getName());

        if(!blacklists.isEmpty())
            response.setResult(blacklists.size() + " kết quả trùng khớp");
        else
            response.setResult("Không có kết quả trùng khớp");

        response.setBlacklists(blacklists);

        return response;
    }
}
