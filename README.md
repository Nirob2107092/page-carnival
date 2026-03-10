# Page Carnival 📚

Page Carnival is a full-stack book discovery and marketplace platform built with Spring Boot, PostgreSQL, Thymeleaf, and Docker. The platform allows users to explore books, manage listings, and purchase books through a secure role-based system.

This project is developed as part of the Software Engineering Lab course and demonstrates a complete software development workflow including project setup, layered architecture, database design, security, testing, Dockerization, CI/CD, and deployment.

---

## Team Members

- Rysul Aman Nirob
- Biposhayan Chakma

---

## Project Objective

The objective of this project is to build a complete web application that demonstrates professional software engineering practices, including:

- clean layered architecture
- authentication and authorization
- REST API design
- relational database design
- Git and GitHub workflow
- Docker containerization
- CI/CD pipeline integration
- deployment to a public platform

---

## Project Theme

Page Carnival is a digital book marketplace and reading platform where users can browse books, manage listings, and place orders.

---

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA

### Frontend

- Thymeleaf
- HTML
- CSS
- JavaScript

### Database

- PostgreSQL

### DevOps / Tools

- Git
- GitHub
- Docker
- GitHub Actions
- Render

### Testing

- JUnit
- Mockito
- Spring Boot Test
- MockMvc

---

## Core Features

- User registration and login
- Role-based authorization
- Admin, Seller, and Buyer roles
- Book management system
- Book listing and browsing
- Order management
- Secure password encryption
- REST API support
- PostgreSQL database integration
- Dockerized deployment setup
- CI/CD workflow support

---

## System Architecture

The application follows a layered architecture:

User / Browser  
↓  
Thymeleaf Views  
↓  
Controller Layer  
↓  
Service Layer  
↓  
Repository Layer  
↓  
PostgreSQL Database

Spring Security is used for authentication, authorization, and role-based access control.

---

## ER Diagram

The core entities of the system are:

- Role
- User
- Book
- Order

Relationships:

- One role can have many users
- One seller can manage many books
- One buyer can place many orders
- One book can be included in many orders
