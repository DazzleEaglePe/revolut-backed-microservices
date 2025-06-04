package com.brunovelasquez.accountservice.service;

import com.brunovelasquez.accountservice.dto.TransferRequest;
import com.brunovelasquez.accountservice.dto.TransferResponse;
import com.brunovelasquez.accountservice.dto.TransactionResponse;
import com.brunovelasquez.accountservice.model.Account;
import com.brunovelasquez.accountservice.model.Transaction;
import com.brunovelasquez.accountservice.model.TransactionType;
import com.brunovelasquez.accountservice.repository.AccountRepository;
import com.brunovelasquez.accountservice.repository.TransactionRepository;
import com.brunovelasquez.accountservice.exceptions.AccountNotFoundException;
import com.brunovelasquez.accountservice.exceptions.InsufficientFundsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    // Crear una transacción
    @Transactional
    public TransactionResponse createTransaction(UUID accountId, BigDecimal amount, TransactionType type) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con ID: " + accountId));

        // Validar si hay fondos suficientes para una operación de retiro
        if (type == TransactionType.WITHDRAWAL && account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes para retiro.");
        }

        // Actualizar el balance de la cuenta
        if (type == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }

        accountRepository.save(account);

        // Registrar la transacción
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setTimestamp(LocalDateTime.now());
        tx = transactionRepository.save(tx);

        return mapToDto(tx);
    }

    // Transferencia entre cuentas
    @Transactional
    public TransferResponse transferBetweenAccounts(TransferRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta.");
        }

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta de origen no encontrada."));
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta de destino no encontrada."));

        BigDecimal amount = request.getAmount();

        // Validar si la cuenta de origen tiene suficientes fondos
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes en la cuenta de origen.");
        }

        // Realizar la transferencia
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Registrar transacciones de retiro y depósito
        registerTransaction(fromAccount, amount, TransactionType.WITHDRAWAL);
        registerTransaction(toAccount, amount, TransactionType.DEPOSIT);

        return new TransferResponse(fromAccount.getId(), fromAccount.getBalance(),
                toAccount.getId(), toAccount.getBalance());
    }

    private void registerTransaction(Account account, BigDecimal amount, TransactionType type) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);
    }

    // Obtener transacciones por cuenta
    public List<TransactionResponse> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Obtener transacciones filtradas por cuenta y tipo
    public List<TransactionResponse> getTransactionsByAccountAndType(UUID accountId, TransactionType type) {
        return transactionRepository.findByAccountIdAndType(accountId, type)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Mapeo de la entidad a DTO
    private TransactionResponse mapToDto(Transaction tx) {
        TransactionResponse dto = new TransactionResponse();
        dto.setId(tx.getId());
        dto.setAmount(tx.getAmount());
        dto.setTimestamp(tx.getTimestamp());
        dto.setType(tx.getType());
        dto.setAccountId(tx.getAccount().getId());
        dto.setNewBalance(tx.getAccount().getBalance());
        return dto;
    }
}
