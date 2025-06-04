// Paso 4: Crear un DTO de salida (TransactionResponse)
// Este DTO representa lo que el frontend verá al hacer una consulta de transacciones.

package com.brunovelasquez.accountservice.dto;

import com.brunovelasquez.accountservice.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionType type;
    private UUID accountId;
    private BigDecimal newBalance; // Nuevo campo agregado
}
