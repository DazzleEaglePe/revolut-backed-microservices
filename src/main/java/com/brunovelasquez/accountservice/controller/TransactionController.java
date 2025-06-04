// Paso 3: Crear el TransactionController
// Esta clase expone las rutas HTTP para crear y consultar transacciones

package com.brunovelasquez.accountservice.controller;

import com.brunovelasquez.accountservice.dto.TransactionRequest;
import com.brunovelasquez.accountservice.dto.TransactionResponse;
import com.brunovelasquez.accountservice.model.TransactionType;
import com.brunovelasquez.accountservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Crear una nueva transacción (retiro o depósito)
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request) {
        TransactionResponse created = transactionService.createTransaction(
                request.getAccountId(),
                request.getAmount(),
                request.getType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Obtener todas las transacciones de una cuenta
    @GetMapping("/by-account")
    public ResponseEntity<List<TransactionResponse>> getByAccount(@RequestParam UUID accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccount(accountId));
    }

    // Obtener todas las transacciones de una cuenta filtradas por tipo (versión robusta)
    @GetMapping("/by-account-and-type")
    public ResponseEntity<List<TransactionResponse>> getByAccountAndType(
            @RequestParam UUID accountId,
            @RequestParam String type
    ) {
        try {
            TransactionType enumType = TransactionType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(transactionService.getTransactionsByAccountAndType(accountId, enumType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
