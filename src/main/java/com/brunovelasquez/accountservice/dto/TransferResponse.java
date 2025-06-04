// Paso 5: Crear un DTO de respuesta para la transferencia
package com.brunovelasquez.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransferResponse {
    private UUID fromAccountId;
    private BigDecimal fromNewBalance;
    private UUID toAccountId;
    private BigDecimal toNewBalance;
}
