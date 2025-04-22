package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.entities.Opinion;
import com.pcrt.Pcrt.repository.OpinionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpinionService {

    @Autowired
    private OpinionRepository opinionRepository;

    public Opinion saveOpinion(Opinion opinion) {
        if (opinion != null) {
            this.opinionRepository.save(opinion);
            return opinion;
        }
        return null;
    }
}
