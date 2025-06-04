
# Revolut Microservices Backend 💸

Este proyecto es una simulación de backend para una plataforma financiera tipo Revolut. Implementado con arquitectura de microservicios usando **Spring Boot** y **PostgreSQL**, incluye la gestión de cuentas y transacciones básicas.

---

## 🚀 Tecnologías Utilizadas

- Java 17
- Spring Boot 3+
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- pgAdmin
- Lombok
- Maven

---

## 🧱 Arquitectura

```
account-service/
├── controller/         # Controladores REST
├── dto/                # Objetos de transferencia de datos
├── model/              # Entidades JPA
├── repository/         # Interfaces JPA
├── service/            # Lógica de negocio
├── exception/          # Manejo de errores personalizados
└── resources/
    └── application.properties
```

---

## 📦 Endpoints Principales

### 📘 Cuenta (Account)
- `POST /accounts` – Crear cuenta
- `GET /accounts?customerId=uuid` – Listar cuentas por cliente
- `PATCH /accounts/{id}/freeze` – Congelar cuenta
- `GET /accounts/all` – Listar todas las cuentas
- `GET /accounts/paged?page=0&size=5` – Cuentas paginadas
- `GET /accounts/filter` – Filtrado avanzado
- `POST /accounts/transfer` – Transferencia entre cuentas

### 💳 Transacciones
- `POST /transactions` – Crear transacción (retiro/depósito)
- `GET /transactions/by-account?accountId=uuid` – Listar transacciones de cuenta
- `GET /transactions/by-account-and-type` – Filtro por tipo

---

## 🐳 Docker

Ejecutar:
```bash
docker-compose up -d
```

Accede a **pgAdmin**:  
📍 `http://localhost:8080`  
🔐 Email: `admin@admin.com`  
🔐 Password: `admin`

---

## 🧪 Probar APIs con Postman

Importa la colección `localhost:8081` y prueba los siguientes ejemplos:

```json
POST /accounts
{
  "customerId": "uuid",
  "currency": "USD",
  "balance": 1000,
  "iban": "ES1234567890..."
}
```

```json
POST /accounts/transfer
{
  "fromAccountId": "uuid",
  "toAccountId": "uuid",
  "amount": 100.00
}
```

---

## 🛡️ Manejo de errores

- `AccountNotFoundException`
- `InsufficientFundsException`
- `IllegalArgumentException` personalizado

---

## 👨‍💻 Autor

- **Nombre**: Dazzle Eagle
- **GitHub**: [@DazzleEaglePe](https://github.com/DazzleEaglePe)

---

## 📃 Licencia

Este proyecto es de libre uso académico y educativo.

---

¡Listo para escalar con más microservicios! 🚀
