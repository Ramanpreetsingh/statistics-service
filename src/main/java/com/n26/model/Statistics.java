package com.n26.model;

import lombok.*;

import java.math.BigDecimal;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Statistics {

    private BigDecimal sum;

    private BigDecimal avg;

    private BigDecimal max;

    private BigDecimal min;

    private Long count;

    public static Statistics buildDefaultStatistic() {
        Statistics defaultStatistic = new Statistics();
        defaultStatistic.setAvg(BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP ));
        defaultStatistic.setSum(BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP ));
        defaultStatistic.setMax(BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP ));
        defaultStatistic.setMin(BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_UP));
        defaultStatistic.setCount(new Long(0));
        return defaultStatistic;
    }

}
