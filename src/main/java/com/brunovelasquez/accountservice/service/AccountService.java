package com.brunovelasquez.accountservice.service;

import com.brunovelasquez.accountservice.model.Account;
import com.brunovelasquez.accountservice.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public Account openAccount(Account account) {
        account.setId(null); // evita que se pase un ID desde el frontend
        account.setFrozen(false);
        return repository.save(account);
    }

    public Account getAccount(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    public List<Account> getAccountsByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId);
    }

    public void freezeAccount(UUID id) {
        Account acc = getAccount(id);
        acc.setFrozen(true);
        repository.save(acc);
    }

    public void deleteAccount(UUID id) {
        Account acc = getAccount(id);
        if (acc.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("No se puede eliminar una cuenta con saldo positivo.");
        }
        repository.deleteById(id);
    }

    public List<Account> getAllAccounts() {
    return repository.findAll();
    }

    public Page<Account> getAccountsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public List<Account> getAccountsByCurrency(String currency) {
    return repository.findByCurrency(currency);
    }

    public List<Account> filterAccounts(String currency, Boolean frozen, BigDecimal minBalance) {
    return repository.findAll().stream()
        .filter(acc -> currency == null || acc.getCurrency().equalsIgnoreCase(currency))
        .filter(acc -> frozen == null || acc.isFrozen() == frozen)
        .filter(acc -> minBalance == null || acc.getBalance().compareTo(minBalance) >= 0)
        .collect(Collectors.toList());
    }

    public List<Account> getAccountsByBalanceRange(BigDecimal min, BigDecimal max) {
    return repository.findByBalanceBetween(min, max);
    }

}
