package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.StatisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.n26.model.Statistics.buildDefaultStatistic;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */
@Component
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    ConcurrentHashMap<Date, Statistics> statisticsMap;

    @Override
    public StatisticsResponse calculateStatistics(Date timestamp) {

        Statistics statistic = buildDefaultStatistic();
        statisticsMap.entrySet().stream().filter(entry -> entry.getKey().compareTo(timestamp) > 0)
                .map(entry -> entry.getValue())
                .forEach(partialStatistic -> {
                    statistic.setCount(partialStatistic.getCount() + statistic.getCount());

                    if (statistic.getMax().compareTo(partialStatistic.getMax()) < 0) {
                        statistic.setMax(partialStatistic.getMax().setScale(2,BigDecimal.ROUND_HALF_UP ));
                    }
                    if (statistic.getMin().compareTo(partialStatistic.getMin()) >= 0 || statistic.getMin().compareTo(BigDecimal.ZERO) == 0) {
                        statistic.setMin(partialStatistic.getMin().setScale(2,BigDecimal.ROUND_HALF_UP ));
                    }

                    statistic.setSum(partialStatistic.getSum().add(statistic.getSum().setScale(2,BigDecimal.ROUND_HALF_UP )));
                });

        if (statistic.getCount() == 0) {
            return buildStatisticsResponse(statistic);
        }
        statistic.setAvg(statistic.getSum().divide(new BigDecimal(statistic.getCount()),2, RoundingMode.HALF_UP));
        return buildStatisticsResponse(statistic);
    }

    private StatisticsResponse buildStatisticsResponse(Statistics statistics){
        StatisticsResponse response = StatisticsResponse.builder().avg(statistics.getAvg().toString()).
                count(statistics.getCount()).
                max(statistics.getMax().toString()).
                min(statistics.getMin().toString()).sum(statistics.getSum().toString()).build();

        return response;
    }
}
