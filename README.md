# 📚 Library Management System

A comprehensive **Library Management System** built with **Spring Boot 3**, **Java 21**, and **PostgreSQL**, designed to streamline book lending operations, user management, and secure authentication with role-based access control.

---

## 🛠️ Tech Stack

| Layer         | Technology                        |
| ------------- | --------------------------------- |
| Backend       | Java 21, Spring Boot 3            |
| Database      | PostgreSQL, H2 (for testing)      |
| Security      | Spring Security, JWT, BCrypt      |
| ORM           | Spring Data JPA, Hibernate        |
| Documentation | Swagger / OpenAPI                 |
| Testing       | JUnit 5, Spring Boot Test         |
| Build Tool    | Maven                             |
| Container     | Docker, Docker Compose            |
| Utilities     | Lombok, MapStruct, Validation API |

---

## 🚀 Features

### 📖 Book Management

* Add, view, update, and delete books
* Search books by title, author, ISBN, or genre
* Pagination support for listing

### 👥 User Management

* User registration with roles: **Librarian**, **Patron**
* Librarians can manage users
* Role-based access with JWT authentication

### 📘 Borrowing System

* Borrow and return books
* Borrow record contains `borrowDate`, `dueDate`, and optional `returnDate`
* Automatic overdue detection (`returnDate == null && dueDate < today`)
* Each user can view their borrowing history

### 🔡️ Security

* JWT-based authentication and Spring Security authorization
* Password encryption using BCrypt
* Role-based access for endpoints

### 📄 API Documentation

* Accessible via [Swagger](https://app.swaggerhub.com/apis/aylin-14f/library-management/1.0.0)
* Fully documented endpoints with request/response examples
* [Postman Collection](https://www.postman.com/research-saganist-83919717/library-management/collection/lwmmio2/library-management?action=share&creator=29648753)

---

## 🧱 Entity Relationship Overview

### Book

```java
UUID id
String title
String author
String genre
String isbn
LocalDate publicationDate
boolean available (dynamic)
List<BorrowRecord> records
```

### User

```java
UUID id
String name
String email
String password
Role role (ENUM: LIBRARIAN, PATRON)
List<BorrowRecord> records
```

### BorrowRecord

```java
UUID id
Book book (ManyToOne)
User user (ManyToOne)
BorrowStatus status (ENUM: BORROWED, RETURNED, OVERDUE)
LocalDate borrowDate
LocalDate dueDate
LocalDate returnDate
```

> Overdue is calculated dynamically by `isOverdue()` method.

---

## 🧪 Testing

* In-memory **H2** database used for isolated testing
* Unit tests for services and controllers
* Authentication, user flows, and borrowing flows covered

Run all tests:

```bash
./mvnw test
```

---

## 🐳 Docker Setup

### Run the application with Docker Compose

```bash
docker-compose up --build
```

### Services

| Name          | Description         |
| ------------- | ------------------- |
| `library-app` | Spring Boot backend |
| `db`          | PostgreSQL database |

---

## 💻 Running Locally (Without Docker)

1. **Clone the repository**

```bash
git clone https://github.com/your-username/library-management-app.git
cd library-management-app
```

2. **Configure the database**

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
```

3. **Run the application**

```bash
./mvnw spring-boot:run
```

---

## 📂 Project Structure

```bash
src
├── main
│   ├── java/com/aylinaygul/librarymanagementapp
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── entity
│   │   ├── exception
│   │   ├── mapper
│   │   ├── repository
│   │   ├── security
│   │   └── service
│   └── resources
│       ├── application.properties
│       ├── application-test.properties
│       └── application-prod.properties
└── test
    └── java/com/aylinaygul/librarymanagementapp
        ├── controller
        └── service
```

---

## 🌐 API Reference

[Swagger Documentation](https://app.swaggerhub.com/apis/aylin-14f/library-management/1.0.0)  or visit after running the app:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📬 Postman Collection

You can find the Postman collection :

https://www.postman.com/research-saganist-83919717/library-management/collection/lwmmio2/library-management?action=share&creator=29648753

---
## 📘 Entity Relationship Diagram


![diagram](https://github.com/user-attachments/assets/d14b0af2-2847-491c-93e1-47836585d45c)


---

## ✅ Best Practices Followed

* Clean architecture (Controller → Service → Repository)
* DTO layer to isolate internal structures
* Role-based method security
* Centralized exception handling using `@ControllerAdvice`
* Dynamic book availability tracking (based on borrow records)
* Clean code & SOLID principles
* Dockerized deployment and multi-env setup

---

## 👩‍💻 Author

Developed by **Aylin Aygül**
🌐 [LinkedIn](https://www.linkedin.com/in/aylinaygul) | [GitHub](https://github.com/aylinaygul)
