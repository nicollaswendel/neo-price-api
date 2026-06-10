![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-darkgreen)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-black)
![License](https://img.shields.io/badge/License-MIT-red)

# 💰 NeoPrice

Pricing management system designed to help small businesses and entrepreneurs calculate selling prices based on costs, profit margins and taxes.

This project was built using **Java + Spring Boot** for the backend and a simple **HTML, CSS and JavaScript** frontend, providing a complete full-stack solution with authentication, customer management and pricing history tracking.

---

## 🚀 Features

* User authentication with JWT
* Customer registration and management
* Automated pricing calculation
* Tax calculation
* Profit margin calculation
* Pricing history tracking
* Secure REST API
* Frontend-backend integration using Fetch API
* Responsive and minimal user interface

---

## 🛠 Tech Stack

### Backend

* Java 25
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* PostgreSQL
* MapStruct
* Lombok

### Frontend

* HTML5
* CSS3
* Vanilla JavaScript
* Fetch API

---

## 📁 Project Structure

```text
neo-price/
│
├── neo-price-backend/     # Spring Boot API
│
└── neo-price-frontend/    # Static frontend
```

---

## ▶️ How to Run the Project

### 1️⃣ Backend

1. Open the backend project in IntelliJ IDEA
2. Configure the database connection in:

```properties
application.properties
```

3. Run the application

API available at:

```text
http://localhost:8080
```

---

### 2️⃣ Frontend

1. Open the frontend folder in VS Code
2. Install the Live Server extension
3. Right-click `index.html`
4. Select **Open with Live Server**

Frontend available at:

```text
http://127.0.0.1:5500
```

---

## 🔐 Authentication

The application uses JWT authentication.

### Login Endpoint

```http
POST /users/login
```

Request:

```json
{
  "email": "user@email.com",
  "password": "password"
}
```

Response:

```json
{
  "token": "jwt-token"
}
```

The token must be included in authenticated requests:

```http
Authorization: Bearer {token}
```

---

## 📦 API Endpoints

### Customers

| Method | Endpoint          | Description        |
| ------ | ----------------- | ------------------ |
| GET    | `/customers`      | List customers     |
| GET    | `/customers/{id}` | Get customer by id |
| POST   | `/customers`      | Create customer    |
| PATCH  | `/customers/{id}` | Update customer    |
| DELETE | `/customers/{id}` | Delete customer    |

### Pricing

| Method | Endpoint        | Description              |
| ------ | --------------- | ------------------------ |
| GET    | `/pricing`      | List pricing history     |
| GET    | `/pricing/{id}` | Get pricing by id        |
| POST   | `/pricing`      | Save pricing calculation |
| DELETE | `/pricing/{id}` | Delete pricing history   |

---

## 💡 Pricing Formula

NeoPrice calculates the ideal selling price using:

* Product cost
* Desired profit margin
* Tax percentage

Generated results include:

* Price without tax
* Final selling price
* Tax amount
* Profit amount
* Profit percentage
* Tax percentage

---

## 🧠 Architecture Highlights

* Layered architecture (Controller, Service, Repository)
* DTO pattern for request and response abstraction
* Entity mapping using MapStruct
* JWT-based authentication and authorization
* Business rules centralized in the service layer
* Relational database modeling with JPA
* Frontend consuming REST endpoints through Fetch API

---

## 🎥 Demonstration

A demonstration video presenting the main features and workflow of the application is available in the repository's About section.

---

## 📜 License

MIT License
