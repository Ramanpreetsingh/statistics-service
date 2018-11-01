package com.n26.model;

import lombok.*;

import java.math.BigDecimal;

/*
 @author Ramanpreet Singh on 02/09/18
 *
 */

// Wrapper class for mapping Statistics class to String response required for Integration test

@Builder
@Data
public class StatisticsResponse {

    private String sum;

    private String avg;

    private String max;

    private String min;

    private Long count;

}
