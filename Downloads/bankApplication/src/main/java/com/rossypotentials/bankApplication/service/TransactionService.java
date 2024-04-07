package com.rossypotentials.bankApplication.service;

import com.rossypotentials.bankApplication.payload.request.TransactionRequest;

public interface TransactionService {
    void saveTransactions(TransactionRequest transactionRequest);
}
