package com.pcrt.Pcrt.controller.api;

import com.pcrt.Pcrt.entities.Transaction;
import com.pcrt.Pcrt.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransactionRestController {
    @Autowired
    private TransactionService transactionService;

    @DeleteMapping("/transaction/{transactionId}/delete")
    public ResponseEntity<Void> deleteTransaction (@PathVariable(value = "transactionId") int transactionId){
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/transaction/update/{transactionId}")
    public ResponseEntity<Void> updateTransactionStatus (@PathVariable(value = "transactionId") int id,
                                                                @RequestParam Map<String, String> params){
        transactionService.updateTransactionStatus(id, params);
        return ResponseEntity.ok().build();
    }
}
