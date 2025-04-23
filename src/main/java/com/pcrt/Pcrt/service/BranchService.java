package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.entities.Branch;
import com.pcrt.Pcrt.repository.BranchRepository;
import com.pcrt.Pcrt.repository.query.BranchRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BranchService {
    @Autowired
    private BranchRepositoryQuery branchRepositoryQuery;
    @Autowired
    private BranchRepository branchRepository;

    public Page<Branch> loadBranchesPage (Map<String, String> params){

        List<Branch> branchList = branchRepositoryQuery.loadBranches(params);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;
        long totalPages = branchRepositoryQuery.countBranch(params);

        return new PageImpl<>(branchList, PageRequest.of(page, 10), totalPages);
    }

    public List<Branch> loadBranchesList (Map<String, String> params){

        return branchRepositoryQuery.loadBranches(params);
    }

    public Branch getBranchById (int branchId){
        return branchRepository.findById(branchId).orElse(null);
    }
}
