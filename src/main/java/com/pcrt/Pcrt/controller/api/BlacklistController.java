package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.dto.request.CustomerCheckingRequest;
import com.pcrt.Pcrt.dto.response.CustomerCheckingResponse;
import com.pcrt.Pcrt.entities.Blacklist;
import com.pcrt.Pcrt.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;

    @GetMapping("/blacklists")
    public List<Blacklist> getBlacklists (){
        return this.blacklistService.getBlacklists();
    }

    @PostMapping("/customer-checking")
    public CustomerCheckingResponse getBlacklistByCustomer (@RequestBody CustomerCheckingRequest request){
        return blacklistService.getBlacklistByCustomer(request);
    }
}
