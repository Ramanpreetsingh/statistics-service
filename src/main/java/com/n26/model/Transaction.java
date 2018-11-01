package com.n26.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/*
 @author Ramanpreet Singh on 31/08/18
 *
 */
@AllArgsConstructor
@Builder
@Data
public class Transaction {

    BigDecimal amount;

    Date timestamp;

}
