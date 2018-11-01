package com.n26.service;

import com.n26.config.ApplicationConfiguration;
import com.n26.model.Statistics;
import com.n26.model.StatisticsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

/*
 @author Ramanpreet Singh on 02/09/18
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        StatisticsServiceImpl.class,
        ApplicationConfiguration.class})
public class StatisticsServiceTests {

    @Autowired
    StatisticsService statisticService;

    @Autowired
    ConcurrentHashMap<Date, Statistics> statisticsMap;

    final Date TIMESTAMP_WINDOW = new Date(System.currentTimeMillis()-60000);

    final List<Date> VALID_TIMES = new ArrayList<Date>() {{
        add(new Date(System.currentTimeMillis()-40000));
        add(new Date(System.currentTimeMillis()-30000));
        add(new Date(System.currentTimeMillis()-20000));
    }};
    final BigDecimal DEFAULT_AMOUNT = new BigDecimal("20.00");

    @Test
    public void testStatisticsCalculationIsCorrect() {
        statisticsMap.clear();
        initStatisticsMap();
        final Statistics expectedStatistic = getExpectedStatisticResponse();
        StatisticsResponse expectedStatisticsResponse = buildStatisticsResponse(expectedStatistic);
        assertEquals(statisticService.calculateStatistics(TIMESTAMP_WINDOW)
                ,expectedStatisticsResponse);
    }

    private void initStatisticsMap() {
        VALID_TIMES.stream().forEach(timestamp -> {
                    statisticsMap.put(timestamp, Statistics.builder()
                            .count(new Long(1))
                            .max(DEFAULT_AMOUNT)
                            .min(DEFAULT_AMOUNT)
                            .sum(DEFAULT_AMOUNT)
                            .build());
                }
        );
    }

    private Statistics getExpectedStatisticResponse() {
        final Statistics expectedStatistic = new Statistics();
        expectedStatistic.setAvg(DEFAULT_AMOUNT);
        expectedStatistic.setCount(new Long(3));
        expectedStatistic.setMax(DEFAULT_AMOUNT);
        expectedStatistic.setMin(DEFAULT_AMOUNT);
        expectedStatistic.setSum(DEFAULT_AMOUNT.multiply(new BigDecimal(3)));
        return expectedStatistic;
    }

    private StatisticsResponse buildStatisticsResponse(Statistics statistics){
        StatisticsResponse response = StatisticsResponse.builder().avg(statistics.getAvg().toString()).
                count(statistics.getCount()).
                max(statistics.getMax().toString()).
                min(statistics.getMin().toString()).sum(statistics.getSum().toString()).build();

        return response;
    }


}
