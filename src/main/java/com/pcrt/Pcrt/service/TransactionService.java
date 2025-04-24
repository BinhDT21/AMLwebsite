package com.pcrt.Pcrt.service;

import com.pcrt.Pcrt.dto.request.CreateReviewRequest;
import com.pcrt.Pcrt.entities.Customer;
import com.pcrt.Pcrt.entities.Review;
import com.pcrt.Pcrt.entities.Transaction;
import com.pcrt.Pcrt.repository.ReviewRepository;
import com.pcrt.Pcrt.repository.TransactionRepository;
import com.pcrt.Pcrt.repository.query.TransactionRepositoryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepositoryQuery transactionRepositoryQuery;
    @Autowired
    private TransactionRepository transactionRepository;



    public List<Transaction> reviewRecordsByAmount(Map<String, String> status) {
        return transactionRepositoryQuery.reviewRecordsByAmount(status);
    }

    public List<Object[]> reviewRecordsByNumberTransaction(Map<String, String> status) {
        return transactionRepositoryQuery.reviewRecordsByNumberTransaction(status);
    }

    // lấy danh sách Transaction của mỗi Customer.nationalId
    public List<Transaction> getTransactionListByNationalId(String nationalId){

        return transactionRepositoryQuery.getTransactionListByNationalId(nationalId);
    }

    // get transaction by id
    public Transaction getTransactionById (int transactionId){
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException());
        return transaction;
    }

    // update status
    public void updateTransactionStatus (int transactionId){
        Transaction transaction = getTransactionById(transactionId);
        transaction.setStatus("staff_checked");

        transactionRepository.save(transaction);
        System.out.println("status update success :)");
    }
    //update status -> pass / report
    public void updateTransactionStatus (int transactionId, Map<String, String> params){

        String status = params.get("status");
        Transaction transaction = getTransactionById(transactionId);
        transaction.setStatus(status);

        transactionRepository.save(transaction);
        System.out.println("status update success :)");
    }

    // update status for list (get list by nationalId in currentDate)
    public void updateListTransactionStatus (String nationalId){
        List<Transaction> transactionList = getTransactionListByNationalId(nationalId);
        for(Transaction t : transactionList){
            updateTransactionStatus(t.getId());
        }
        System.out.println("status update success :))");
    }
    public List<Transaction> getTransactionListByCustomer (int customerId){

        return transactionRepositoryQuery.getTransactionListByCustomer(customerId);
    }


    //get transaction amount of date by customer id filter with status = pending
    public BigDecimal getTransactionAmountOfDateByCustomerId (int customerId){
        List<Transaction> transactionList = transactionRepositoryQuery.getTransactionListByCustomer(customerId);

        // remove transaction of customer if the status equal pending
        transactionList.removeIf(transaction -> (transaction.getStatus().equals("pending")));
        BigDecimal result = new BigDecimal(0);
        for (Transaction transaction : transactionList){
            result = result.add(transaction.getAmount());
        }

        return result;
    }

    //get frequency of date by customer id filter with status = pending
    public int getFrequencyOfDateByCustomerId (int customerId){
        List<Transaction> transactionList = transactionRepositoryQuery.getTransactionListByCustomer(customerId);
        // remove transaction of customer if the status equal pending
        transactionList.removeIf(transaction -> (transaction.getStatus().equals("pending")));

        return transactionList.size();
    }

    //get frequency of date by customer id
    public int getFrequencyOfDateByCustomerId2 (int customerId){
        List<Transaction> transactionList = transactionRepositoryQuery.getTransactionListByCustomer(customerId);
        transactionList.removeIf(transaction -> !(transaction.getStatus().equals("pending") || transaction.getStatus().equals("staff_checked")));
        return transactionList.size();
    }

    //get transaction amount of date by customer id
    public BigDecimal getTransactionAmountOfDateByCustomerId2 (int customerId){
        List<Transaction> transactionList = transactionRepositoryQuery.getTransactionListByCustomer(customerId);
        transactionList.removeIf(transaction -> !(transaction.getStatus().equals("pending") || transaction.getStatus().equals("staff_checked")));

        BigDecimal result = new BigDecimal(0);
        for (Transaction transaction : transactionList){
            result = result.add(transaction.getAmount());
        }

        return result;
    }

    //get transaction List with params: status, createdDate
    public Page<Transaction> getTransactionList (Map<String, String> params){

        List<Transaction> transactionList = transactionRepositoryQuery.getTransactionList(params);
        long totalTransactions = transactionRepositoryQuery.countTransaction(params);
        int page = params.get("page")!=null?Integer.parseInt(params.get("page")):0;

        Page<Transaction> pageTransaction = new PageImpl<>(transactionList, PageRequest.of(page,20), totalTransactions);

        return pageTransaction;
    }

    //delete transaction
    public void deleteTransaction (int transactionId){
        transactionRepository.delete(getTransactionById(transactionId));
    }


}
