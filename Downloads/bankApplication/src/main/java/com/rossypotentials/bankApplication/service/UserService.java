package com.rossypotentials.bankApplication.service;

import com.rossypotentials.bankApplication.payload.request.CreditAndDebitRequest;
import com.rossypotentials.bankApplication.payload.request.EnquiryRequest;
import com.rossypotentials.bankApplication.payload.request.TransferRequest;
import com.rossypotentials.bankApplication.payload.response.BankResponse;

public interface UserService {
    BankResponse creditAccount (CreditAndDebitRequest request);
    BankResponse debitAccount (CreditAndDebitRequest request);
    BankResponse enquiryRequest (EnquiryRequest enquiryRequest);
    String nameEquiry(EnquiryRequest enquiryRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
