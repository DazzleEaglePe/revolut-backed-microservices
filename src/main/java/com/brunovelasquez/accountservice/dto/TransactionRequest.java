package com.brunovelasquez.accountservice.dto;

import com.brunovelasquez.accountservice.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {
    private UUID accountId;
    private BigDecimal amount;
    private TransactionType type;
}
