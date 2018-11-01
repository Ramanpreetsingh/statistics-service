package com.n26.service;

import com.n26.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */

@Component
public interface TransactionService {

    void addTransaction(Transaction transaction, Date transactionArrivalTimestamp) throws Exception;

    void deleteTransactions();
}
