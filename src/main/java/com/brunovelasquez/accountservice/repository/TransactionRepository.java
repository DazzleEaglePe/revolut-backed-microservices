// Paso 1: Crear el TransactionRepository
// Este archivo define la interfaz de acceso a datos para la entidad Transaction.

package com.brunovelasquez.accountservice.repository;

import com.brunovelasquez.accountservice.model.Transaction;
import com.brunovelasquez.accountservice.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Buscar todas las transacciones por ID de cuenta
    List<Transaction> findByAccountId(UUID accountId);

    // Buscar por tipo y cuenta (opcional para filtros posteriores)
    List<Transaction> findByAccountIdAndType(UUID accountId, TransactionType type);
}

/*
✅ Explicación:
- JpaRepository<Transaction, Long>: le decimos que vamos a trabajar con la entidad Transaction y su ID es tipo Long.
- Spring Boot generará automáticamente los métodos básicos (save, findAll, deleteById, etc.).
- Agregamos un método derivado (findBy...) para buscar todas las transacciones de una cuenta.
*/
