package com.rossypotentials.bankApplication.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rossypotentials.bankApplication.domain.entity.Transactions;
import com.rossypotentials.bankApplication.domain.entity.UserEntity;
import com.rossypotentials.bankApplication.payload.request.EmailDetails;
import com.rossypotentials.bankApplication.repository.TransactionRepository;
import com.rossypotentials.bankApplication.repository.UserRepository;
import com.rossypotentials.bankApplication.service.EmailService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor
public class BankStatementImpl {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String FILE = System.getProperty("user.home") + "/Desktop/BankStatement.pdf";
    public List <Transactions> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List <Transactions> transactionsList = transactionRepository.findAll()
                .stream().filter(transactions -> transactions.getAccountNumber().equals(accountNumber))
                .filter(transactions -> transactions.getCreatedAt().isEqual(start))
                .filter(transactions -> transactions.getCreatedAt().isEqual(end)).toList();
        UserEntity user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
        String customerAddress = user.getAddress();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();


        //Table
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Ipex Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GREEN);
        bankName.setPadding(20f);

        //Address
        PdfPCell bankAddress = new PdfPCell(new Phrase("No 20: Federal Housing"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);


        PdfPTable statementInfo = new PdfPTable(1);
        PdfPCell dateFrom = new PdfPCell(new Phrase("Start Date: " + startDate));
        dateFrom.setBorder(0);

        //TITLE OF THE DOCUMENT SETTING
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);

        PdfPCell dateTo = new PdfPCell(new Phrase("End Date " + endDate));
        dateTo.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer   Name " + customerName));
        name.setBorder(0);

        PdfPCell space = new PdfPCell();
        space.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase("Address " +  customerAddress));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.GREEN);
        date.setBorder(0);

        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.GREEN);
        transactionType.setBorder(0);

        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.GREEN);
        transactionAmount.setBorder(0);

        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.GREEN);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        transactionsList.forEach(transactions -> {
            transactionTable.addCell(new Phrase(transactions.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transactions.getTransactionType()));
            transactionTable.addCell(new Phrase(transactions.getAmount().toString()));
            transactionTable.addCell(new Phrase(transactions.getStatus()));

        });

            statementInfo.addCell(dateFrom);
            statementInfo.addCell(statement);
            statementInfo.addCell(dateTo);
            statementInfo.addCell(name);
            statementInfo.addCell(space);
            statementInfo.addCell(address);


            document.add(bankInfoTable);
            document.add(statementInfo);
            document.add(transactionTable);

            document.close();

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(user.getEmail())
                    .subject("STATEMENT OF ACCOUNT")
                    .messageBody("Find attached your requested statement of Account")
                    .attachment(FILE)
                    .build();
            emailService.sendEmailWithAttachment(emailDetails);
            return transactionsList;


    }
}
