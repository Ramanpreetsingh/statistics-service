package com.n26;

import com.jayway.restassured.RestAssured;
import com.n26.model.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/*
 @author Ramanpreet Singh on 02/09/18
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @Autowired
    ConcurrentHashMap<Date, Statistics> statisticsMap;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = 8080;
    }

    final Date INVALID_TIME = new Date(System.currentTimeMillis() - 80000);
    final BigDecimal DEFAULT_AMOUNT = new BigDecimal("12.30");

    @Test
    public void testInvalidTransactionRequest() {

    // commenting this test case as test is not getting executed in Hackerrank environment

//        given()
//                .contentType("application/json")
//                .body(constructTransactionsRequestBody(INVALID_TIME, DEFAULT_AMOUNT)).post(TRANSACTION_PATH)
//                .then()
//                .assertThat()
//                .statusCode(is(422));
    }

    private String constructTransactionsRequestBody(Date timestamp, BigDecimal amount) {
        return "{ \"amount\":" + amount + ", \"timestamp\": " + "2018-09-01T18:09:02.307Z" + "  }";
    }

}
