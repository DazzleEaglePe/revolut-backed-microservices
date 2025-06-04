// Paso 1: Crear un DTO para la transferencia de cuenta a cuenta
package com.brunovelasquez.accountservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferRequest {
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
}
