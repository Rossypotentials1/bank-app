package com.rossypotentials.bankApplication.service.impl;

import com.rossypotentials.bankApplication.domain.entity.Transactions;
import com.rossypotentials.bankApplication.payload.request.TransactionRequest;
import com.rossypotentials.bankApplication.repository.TransactionRepository;
import com.rossypotentials.bankApplication.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    @Override
    public void saveTransactions(TransactionRequest transactionRequest) {
        Transactions transactions = Transactions.builder()
                .transactionType(transactionRequest.getTransactionType())
                .accountNumber(transactionRequest.getAccountNumber())
                .amount(transactionRequest.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transactions);



        System.out.println("Transaction Saved Successfully");
    }
}
