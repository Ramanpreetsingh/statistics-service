package com.n26.config;

import com.n26.model.Statistics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/*
 @author Ramanpreet Singh on 01/09/18
 *
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    public ConcurrentHashMap<Date, Statistics> statisticsMap() {
        return new ConcurrentHashMap<>();
    }

}
