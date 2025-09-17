# Order Management System - Hexagonal Architecture Demo

A comprehensive order management system demonstrating **Hexagonal Architecture** (Ports & Adapters) pattern using Java 21, Spring Boot, REST API, and MySQL integration.

## 🏗️ Architecture Overview

This project implements the **Hexagonal Architecture** pattern, which provides:
- Clear separation between business logic and external dependencies
- Testable and maintainable codebase
- Flexible integration with external systems
- Dependency inversion principle enforcement

### Architecture Layers

#### 1. **Domain Layer** (Core Business Logic)
- **Entities**: `Order`, `Customer`, `OrderItem`, `Money`
- **Value Objects**: `OrderId`, `CustomerId`, `OrderStatus`
- **Ports**: Interfaces for external dependencies (`OrderRepository`, `PaymentService`, `EmailService`)

#### 2. **Application Layer** (Use Cases)
- **Use Cases**: `CreateOrderUseCase`, `ConfirmOrderUseCase`, `GetOrderUseCase`, `CreateCustomerUseCase`
- Orchestrates business operations
- Implements application-specific business rules

#### 3. **Infrastructure Layer** (External Adapters)
- **REST Controllers**: API endpoints for order and customer management
- **Database Adapters**: JPA repositories and entity mappings
- **External Service Adapters**: Payment processing and email notifications

## 🚀 Core Features

### Order Management
- ✅ **Create Orders**: Add new orders with multiple items
- ✅ **Confirm Orders**: Process order confirmation with payment
- ✅ **Retrieve Orders**: Get order details and status
- ✅ **Payment Processing**: Integrated payment handling
- ✅ **Email Notifications**: Automatic confirmation emails

### Customer Management
- ✅ **Create Customers**: Register new customers
- ✅ **Customer Validation**: Email and data validation

## 🛠️ Technology Stack

- **Java 21**: Latest LTS version with modern features
- **Spring Boot 3.2.0**: Application framework
- **Spring Data JPA**: Database abstraction
- **MySQL**: Production database
- **H2 Database**: In-memory database for testing
- **JUnit 5**: Testing framework
- **Maven**: Build and dependency management

## 📁 Project Structure

```
src/
├── main/java/com/hexarch/demo/
│   ├── domain/
│   │   ├── model/          # Domain entities and value objects
│   │   └── ports/          # Interfaces for external dependencies
│   ├── application/
│   │   └── usecases/       # Business use cases
│   ├── infrastructure/
│   │   └── adapters/
│   │       ├── persistence/ # Database adapters
│   │       ├── rest/        # REST API controllers
│   │       ├── payment/     # Payment service adapters
│   │       └── email/       # Email service adapters
│   └── OrderManagementApplication.java
└── test/                   # Comprehensive test suite
```

## 🔧 Setup & Installation

### Prerequisites
- Java 21+
- Maven 3.6+
- MySQL 8.0+ (for production)

### Database Setup

1. **MySQL Production Database**:
   ```sql
   CREATE DATABASE order_management;
   ```

2. **Configure Connection** (application.properties):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/order_management
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Build & Run

1. **Clone and Build**:
   ```bash
   git clone <repository-url>
   cd hex-arch-demo-autobuild
   mvn clean compile
   ```

2. **Run Tests**:
   ```bash
   mvn test
   ```

3. **Start Application**:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Customer Endpoints

#### Create Customer
```http
POST /api/customers
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Response**:
```json
{
  "id": "uuid-here",
  "name": "John Doe",
  "email": "john@example.com"
}
```

### Order Endpoints

#### Create Order
```http
POST /api/orders
Content-Type: application/json

{
  "customerId": "customer-uuid",
  "items": [
    {
      "productName": "Product Name",
      "unitPrice": 10.00,
      "currency": "USD",
      "quantity": 2
    }
  ]
}
```

#### Get Order
```http
GET /api/orders/{orderId}
```

#### Confirm Order
```http
POST /api/orders/{orderId}/confirm
```

**Response**:
```json
{
  "id": "order-uuid",
  "customerId": "customer-uuid",
  "status": "PAID",
  "totalAmount": 20.00,
  "currency": "USD",
  "createdAt": "2023-12-01T10:00:00",
  "confirmedAt": "2023-12-01T10:05:00",
  "items": [...]
}
```

## 🧪 Testing

The project includes comprehensive testing at all levels:

### Unit Tests
- **Domain Logic**: `OrderTest`, `CustomerTest`
- **Use Cases**: `CreateOrderUseCaseTest`, `ConfirmOrderUseCaseTest`

### Integration Tests
- **End-to-End**: `OrderManagementIntegrationTest`
- **API Testing**: Full workflow validation

### Test Configuration
- Uses H2 in-memory database for tests
- Transactional test rollback
- Mock external services

Run tests with:
```bash
mvn test
```

## 🎯 Hexagonal Architecture Benefits Demonstrated

1. **Dependency Inversion**: Business logic doesn't depend on external frameworks
2. **Testability**: Easy to unit test business logic with mocks
3. **Flexibility**: Can swap database or external services easily
4. **Maintainability**: Clear separation of concerns
5. **Domain-Driven Design**: Rich domain model with business rules

## 🔄 Order Workflow

1. **Customer Creation**: Register customer in the system
2. **Order Creation**: Create order with items and customer association
3. **Order Confirmation**: Trigger payment processing and email notification
4. **Payment Processing**: Handle payment through external service
5. **Email Notification**: Send confirmation email to customer

## 📈 Extension Points

The architecture makes it easy to extend:

- **New Payment Providers**: Implement `PaymentService` interface
- **Different Notification Methods**: Implement `EmailService` interface  
- **Additional Databases**: Implement repository interfaces
- **New Use Cases**: Add new use case classes
- **API Versions**: Add new controller versions

## 🏁 Conclusion

This project demonstrates a production-ready implementation of Hexagonal Architecture, showcasing best practices for building maintainable, testable, and flexible enterprise applications.