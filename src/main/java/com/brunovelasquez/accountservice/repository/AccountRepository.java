package com.brunovelasquez.accountservice.repository;

import com.brunovelasquez.accountservice.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByCustomerId(UUID customerId);
    Page<Account> findAll(Pageable pageable);
    List<Account> findByCurrency(String currency);
    List<Account> findByBalanceBetween(BigDecimal min, BigDecimal max); //Add method for calculate balance min and max

}
