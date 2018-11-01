package com.n26.service;

import com.n26.exception.StaleTransactionException;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */

@Component
public class TransactionServiceImpl implements TransactionService {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    ConcurrentHashMap<Date, Statistics> statisticsMap;

    @Override
    public void addTransaction(Transaction transaction, Date lastAcceptedTimestamp) throws Exception{

        validateTransaction(transaction, lastAcceptedTimestamp);
        addTransactionRecord(transaction, lastAcceptedTimestamp);
    }

    @Override
    public void deleteTransactions() {
        statisticsMap.clear();
    }

    private void validateTransaction(Transaction transaction, Date lastAcceptedTimestamp) throws Exception {

        if (lastAcceptedTimestamp.compareTo(transaction.getTimestamp())>0){
            LOGGER.debug("Transaction is older than 60 seconds, rejecting it {} {}",transaction.getTimestamp(),lastAcceptedTimestamp);
            throw new StaleTransactionException();
        }

        if (transaction.getTimestamp().compareTo(new Date(System.currentTimeMillis())) > 0) {
            LOGGER.debug("Transaction is from future {} {}",transaction.getTimestamp(),new Date(System.currentTimeMillis()));
            throw new IllegalArgumentException();
        }
    }

    private void addTransactionRecord(Transaction transaction, Date lastAcceptedTimestamp) {

        if (statisticsMap.containsKey(transaction.getTimestamp())) {
            Statistics statistics = statisticsMap.get(transaction.getTimestamp());
            statistics.setCount(statistics.getCount() + 1);
            if (statistics.getMax().compareTo(transaction.getAmount()) < 1) {
                statistics.setMax(transaction.getAmount());
            }
            if (statistics.getMin().compareTo(transaction.getAmount()) > 1) {
                statistics.setMin(transaction.getAmount());
            }
            statistics.setSum(statistics.getSum().add(transaction.getAmount()));
            //statistics.setAvg(statistics.getSum().divide(new BigDecimal(statistics.getCount()), 2, RoundingMode.HALF_UP));
        } else {
            Statistics statistics = Statistics.builder().
                    count(new Long(1)).
                    max(transaction.getAmount()).
                    min(transaction.getAmount()).
                    sum(transaction.getAmount()).build();
            statisticsMap.put(transaction.getTimestamp(), statistics);
        }

        removeOldTransactions(lastAcceptedTimestamp);
    }

    private void removeOldTransactions(Date lastAcceptedTimestamp){

        List<Date> outDatedStatisticKeys = statisticsMap
                .keySet()
                .stream()
                .filter(timestamp -> timestamp.compareTo(lastAcceptedTimestamp) < 0)
                .collect(Collectors.<Date>toList());
        outDatedStatisticKeys.forEach(timestamp -> statisticsMap.remove(timestamp));
    }
}
