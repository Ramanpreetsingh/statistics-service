package com.n26.service;

import com.n26.model.StatisticsResponse;

import java.util.Date;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */
public interface StatisticsService {

    StatisticsResponse calculateStatistics(Date timestamp);
}
