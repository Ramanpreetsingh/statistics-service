package com.n26.service;

import com.n26.config.ApplicationConfiguration;
import com.n26.exception.StaleTransactionException;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static com.n26.constants.Constants.TRANSACTION_WINDOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 @author Ramanpreet Singh on 02/09/18
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        TransactionServiceImpl.class,
        ApplicationConfiguration.class})
public class TransactionServiceTests {

    @Autowired
    TransactionService transactionService;

    @Autowired
    ConcurrentHashMap<Date, Statistics> statisticsMap;

    final Date VALID_TIME = new Date(System.currentTimeMillis() - 40000);
    final Date LATEST_VALID_TIME = new Date(System.currentTimeMillis() - TRANSACTION_WINDOW);
    final Date NOT_VALID_TIME = new Date(System.currentTimeMillis() - 90000);
    final BigDecimal DEFAULT_AMOUNT = new BigDecimal("93.00");

    @Test(expected = StaleTransactionException.class)
    public void testThatTransactionOlderThan60Seconds() throws Exception {
        Transaction notValidTransaction = createTransaction(NOT_VALID_TIME);
        transactionService.addTransaction(notValidTransaction, LATEST_VALID_TIME);
    }


    @Test
    public void testThatPartialStatisticCountIsCorrect() throws Exception {
        statisticsMap.clear();
        final Transaction transaction = createTransaction(VALID_TIME);
        Statistics expectedPartiaStatistic = createPartialStatictics(1);
        transactionService.addTransaction(transaction, LATEST_VALID_TIME);
        assertTrue(statisticsMap.containsKey(VALID_TIME));
        assertEquals(statisticsMap.get(VALID_TIME), expectedPartiaStatistic);

    }

    @Test
    public void testStatisticCalculationMultipleRequests() {
        statisticsMap.clear();
        Statistics expectedPartialStatistic = createPartialStatictics(3);
        Transaction validTransaction = createTransaction(VALID_TIME);
        IntStream.range(0, 3).forEach(i ->
        {
            try {
                transactionService.addTransaction(validTransaction, LATEST_VALID_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        assertTrue(statisticsMap.containsKey(VALID_TIME));
        assertEquals(statisticsMap.get(VALID_TIME), expectedPartialStatistic);

    }

    @Test
    public void testThatStatisicCalculationNotIncludingOutdatedTransactions() throws Exception {
        Statistics expectedPartialStatistic = createPartialStatictics(1);
        Statistics outDatedPartialStatistic = createPartialStatictics(3);
        Transaction validTransaction = createTransaction(VALID_TIME);
        statisticsMap.clear();
        statisticsMap.put(NOT_VALID_TIME, outDatedPartialStatistic);
        transactionService.addTransaction(validTransaction, LATEST_VALID_TIME);
        assertTrue(statisticsMap.containsKey(VALID_TIME));
        assertEquals(statisticsMap.get(VALID_TIME), expectedPartialStatistic);
        assertEquals(statisticsMap.size(), 1);
    }

    private Transaction createTransaction(final Date timestamp) {
        return Transaction.builder()
                .amount(DEFAULT_AMOUNT)
                .timestamp(timestamp)
                .build();
    }

    private Statistics createPartialStatictics(int count) {
        return Statistics.builder()
                .count(new Long(count))
                .max(DEFAULT_AMOUNT)
                .min(DEFAULT_AMOUNT)
                .sum(DEFAULT_AMOUNT.multiply(new BigDecimal(count)))
                .build();
    }

}
