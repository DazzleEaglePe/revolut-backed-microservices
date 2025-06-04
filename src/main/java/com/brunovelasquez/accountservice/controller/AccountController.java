package com.brunovelasquez.accountservice.controller;

import com.brunovelasquez.accountservice.dto.TransferRequest;
import com.brunovelasquez.accountservice.dto.TransferResponse;
import com.brunovelasquez.accountservice.model.Account;
import com.brunovelasquez.accountservice.service.AccountService;
import com.brunovelasquez.accountservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request) {
    TransferResponse response = transactionService.transferBetweenAccounts(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping //Endpoint create a account  method post http://localhost:8081/accounts
    public ResponseEntity<Account> create(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.openAccount(account));
    }

    @GetMapping
    public List<Account> listByCustomerId(@RequestParam UUID customerId) {
        return service.getAccountsByCustomerId(customerId);
    }

    @PatchMapping("/{id}/freeze")
    public ResponseEntity<Void> freeze(@PathVariable UUID id) {
        service.freezeAccount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
    Account account = service.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }

    @GetMapping("/paged") //Add peagble
    public ResponseEntity<Page<Account>> getPagedAccounts(
       @RequestParam(defaultValue = "0") int page,
       @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(service.getAccountsPaged(page,size));
    }

    @GetMapping("/by-currency")
    public ResponseEntity<List<Account>> getByCurrency(@RequestParam String currency) {
        return ResponseEntity.ok(service.getAccountsByCurrency(currency));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Account>> filterAccounts(
          @RequestParam(required = false) String currency,
          @RequestParam(required = false) Boolean frozen,
          @RequestParam(required = false) BigDecimal minBalance
    ){
        return ResponseEntity.ok(service.filterAccounts(currency, frozen, minBalance));
    }

    @GetMapping("/by-balance-range")  //Endpoint add in controller calculate range min and max http://localhost:8081/accounts/by-balance-range?min=1000&max=2000
    public ResponseEntity<List<Account>> getByBalanceRange(
        @RequestParam BigDecimal min,
        @RequestParam BigDecimal max) {
        return ResponseEntity.ok(service.getAccountsByBalanceRange(min, max));
    }

}
