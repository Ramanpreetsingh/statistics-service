package com.n26.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.controller.TransactionController;
import com.n26.exception.InvalidJsonException;
import com.n26.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/*
 @author Ramanpreet Singh on 02/09/18
 *
 */
@Component
public class TransactionHelper {

    private static Logger LOGGER = LoggerFactory.getLogger(TransactionHelper.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public Transaction getTransaction(String requestBody){
        Transaction transaction = null;
        try {
            JsonNode jsonNode = mapper.readValue(requestBody, JsonNode.class);

            if(jsonNode.get("timestamp")==null || jsonNode.get("amount")==null){
                throw new InvalidJsonException();
            }
            String date = jsonNode.get("timestamp").textValue();
            Calendar calendar = DatatypeConverter.parseDateTime(date);
            Date timestamp = calendar.getTime();

            String amountString = jsonNode.get("amount").textValue();
            transaction =  new Transaction(new BigDecimal(amountString),timestamp);

        } catch (IOException e) {
            LOGGER.info("Non parsable request received",e);
            throw new IllegalArgumentException();
        }

        return transaction;
    }

}
