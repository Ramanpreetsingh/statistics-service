package com.n26.controller;

import com.n26.helper.TransactionHelper;
import com.n26.model.Transaction;
import com.n26.service.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.n26.constants.Constants.TRANSACTION_PATH;
import static com.n26.constants.Constants.TRANSACTION_WINDOW;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */

@RestController
public class TransactionController {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionHelper transactionHelper;

    @Autowired
    TransactionServiceImpl transactionService;

    @RequestMapping(value = TRANSACTION_PATH, method = RequestMethod.POST)
    public ResponseEntity<Void> createTransaction(@RequestBody String requestBody) throws Exception{

        LOGGER.info("Transaction create request received is {}",requestBody);

        Date lastAcceptedTimestamp = new Date(System.currentTimeMillis() - TRANSACTION_WINDOW);
        Transaction transaction  = transactionHelper.getTransaction(requestBody);
        transactionService.addTransaction(transaction,lastAcceptedTimestamp);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = TRANSACTION_PATH, method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTransactions(){

        transactionService.deleteTransactions();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
