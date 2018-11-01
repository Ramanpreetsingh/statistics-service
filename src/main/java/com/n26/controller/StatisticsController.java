package com.n26.controller;

import com.n26.constants.Constants;
import com.n26.model.StatisticsResponse;
import com.n26.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.n26.constants.Constants.STATISTICS_PATH;
import static com.n26.constants.Constants.TRANSACTION_WINDOW;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */

@RestController
public class StatisticsController {

    private static Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    StatisticsService statisticsService;

    @RequestMapping(value = STATISTICS_PATH, method = RequestMethod.GET)
    public ResponseEntity<StatisticsResponse> getStatistics(){

        Date currentTimestamp = new Date(System.currentTimeMillis()- TRANSACTION_WINDOW);
        StatisticsResponse overallStatistics =  statisticsService.calculateStatistics(currentTimestamp);
        LOGGER.info("Statistics service response is {}",overallStatistics);

        return new ResponseEntity<StatisticsResponse>(overallStatistics, HttpStatus.OK);
    }

}
