package com.rossypotentials.bankApplication.service.impl;

import com.rossypotentials.bankApplication.domain.entity.UserEntity;
import com.rossypotentials.bankApplication.payload.request.*;
import com.rossypotentials.bankApplication.payload.response.AccountInfo;
import com.rossypotentials.bankApplication.payload.response.BankResponse;
import com.rossypotentials.bankApplication.repository.UserRepository;
import com.rossypotentials.bankApplication.service.EmailService;
import com.rossypotentials.bankApplication.service.TransactionService;
import com.rossypotentials.bankApplication.service.UserService;
import com.rossypotentials.bankApplication.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest request) {
        //check if account exists before crediting account
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return  BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " + request.getAmount() + " from " + userToCredit.getFirstName()
                + " Your Account balance is " + userToCredit.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);


        //Save Credit Transactions
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransactions(transactionRequest);
        userRepository.save(userToCredit);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())

                .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {
        //check if account exists before debitting account
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return  BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if (availableBalance.intValue() < debitAmount.intValue()) {

            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current Account balance is "
                    + userToDebit.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(debitAlert);

            //Save Debit Transactions
            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();
            transactionService.saveTransactions(transactionRequest);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " +  userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse enquiryRequest(EnquiryRequest enquiryRequest) {
        //check if account exists before checking  account balance
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return  BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public String nameEquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE;
        }
        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if(!isDestinationAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        UserEntity sourceAccountUser = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());
        if(transferRequest.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccountUser);

        String sourceUsername = sourceAccountUser.getFirstName() +  " " +    sourceAccountUser.getLastName();

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of " + transferRequest.getAmount() + " has been deducted from your account! Your current Account balance is "
                        + sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);

        UserEntity destinationAccountUser = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferRequest.getAmount()));

        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(destinationAccountUser.getEmail())
                .messageBody("Your account has been credited with " + transferRequest.getAmount() + " from " + destinationAccountUser.getFirstName()
                        + " Your Account balance is " + destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);

        //Save Transfer Transactions
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("TRANSACTION")
                .amount(transferRequest.getAmount())
                .build();
    transactionService.saveTransactions(transactionRequest);
       return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
