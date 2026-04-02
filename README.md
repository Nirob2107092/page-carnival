# Page Carnival

Page Carnival is a full-stack book marketplace platform built with Spring Boot, PostgreSQL, Thymeleaf, and Docker. The platform allows users to browse books, manage listings, and purchase books through a secure role-based system with three distinct roles: Admin, Seller, and Buyer.

Developed as part of the Software Engineering Lab course (CSE 3220) to demonstrate a complete professional software development workflow.

---

## Team Members

- Rysul Aman Nirob
- Biposhayan Chakma

---

## Tech Stack

| Layer | Technologies |
|-------|-------------|
| Backend | Java 17, Spring Boot 4.0, Spring Security, Spring Data JPA |
| Frontend | Thymeleaf, HTML, CSS, JavaScript |
| Database | PostgreSQL 15, H2 (testing) |
| DevOps | Docker, Docker Compose, GitHub Actions |
| Testing | JUnit 5, Mockito, SpringBootTest, MockMvc |
| Deployment | Render |

---

## System Architecture

The application follows a layered architecture pattern:

```
User / Browser
      |
Thymeleaf Views (HTML templates)
      |
Controller Layer (8 controllers: REST API + MVC)
      |
Service Layer (interfaces + implementations)
      |
Repository Layer (Spring Data JPA)
      |
PostgreSQL Database
```

Spring Security handles authentication via form login and HTTP Basic, with role-based access control enforced at both URL-level (`SecurityConfig`) and method-level (`@PreAuthorize`).

---

## ER Diagram

![ER Diagram](docs/ER_diagram.png)

### Entities and Relationships

| Relationship | Type | Description |
|-------------|------|-------------|
| Role → User | One-to-Many | One role can have many users |
| User → Book | One-to-Many | One seller can list many books |
| User → Order | One-to-Many | One buyer can place many orders |
| Order → OrderItem | One-to-Many | One order contains many items |
| Book → OrderItem | One-to-Many | One book can appear in many order items |
| Order ↔ Book | Many-to-Many | Linked through OrderItem join entity (carries quantity, unit price, subtotal) |

### Database Schema

| Table | Key Columns |
|-------|------------|
| `roles` | id, name (ADMIN / SELLER / BUYER) |
| `users` | id, full_name, email, password (BCrypt), enabled, role_id (FK) |
| `books` | id, title, author, description, price, stock, category, seller_id (FK) |
| `orders` | id, buyer_id (FK), total_price, order_date, status (PENDING / CONFIRMED / CANCELLED) |
| `order_items` | id, order_id (FK), book_id (FK), quantity, unit_price, subtotal |

---

## API Endpoints

### Authentication (Public)

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/register` | None | Show registration form |
| POST | `/register` | None | Register new user |
| GET | `/login` | None | Show login page |

### REST API -- Books

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/api/books` | Authenticated | List all books |
| GET | `/api/books/{id}` | Authenticated | Get book by ID |
| POST | `/api/books` | SELLER | Create a book |
| PUT | `/api/books/{id}` | SELLER | Update a book |
| DELETE | `/api/books/{id}` | SELLER | Delete a book |

### REST API -- Cart

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/api/cart` | BUYER | View cart |
| POST | `/api/cart/items` | BUYER | Add item to cart |
| PATCH | `/api/cart/items/{bookId}` | BUYER | Update item quantity |
| DELETE | `/api/cart/items/{bookId}` | BUYER | Remove item from cart |
| DELETE | `/api/cart` | BUYER | Clear cart |

### REST API -- Orders

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| POST | `/api/orders` | BUYER | Place order |
| GET | `/api/orders` | BUYER | Get order history |
| GET | `/api/orders/{id}` | BUYER | Get order by ID |
| PUT | `/api/orders/{id}` | BUYER | Update order status |
| DELETE | `/api/orders/{id}` | BUYER | Delete order |

### MVC -- Seller Book Management

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/seller/books` | SELLER | List seller's books |
| GET | `/seller/books/create` | SELLER | Show create form |
| POST | `/seller/books/create` | SELLER | Create book |
| GET | `/seller/books/edit/{id}` | SELLER | Show edit form |
| PUT | `/seller/books/edit/{id}` | SELLER | Update book |
| DELETE | `/seller/books/delete/{id}` | SELLER | Delete book |

### MVC -- Buyer Cart and Orders

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/buyer/cart` | BUYER | View cart page |
| POST | `/buyer/cart/add` | BUYER | Add to cart |
| PATCH | `/buyer/cart/update` | BUYER | Update cart item |
| DELETE | `/buyer/cart/remove/{bookId}` | BUYER | Remove from cart |
| DELETE | `/buyer/cart/clear` | BUYER | Clear cart |
| POST | `/buyer/orders/place` | BUYER | Place order |
| GET | `/buyer/orders/history` | BUYER | View order history |

### MVC -- Dashboards

| Method | URL | Auth | Description |
|--------|-----|------|-------------|
| GET | `/` | None | Home page |
| GET | `/admin/dashboard` | ADMIN | Admin dashboard |
| GET | `/seller/dashboard` | SELLER | Seller dashboard |
| GET | `/buyer/dashboard` | BUYER | Buyer dashboard |
| GET | `/buyer/catalog` | BUYER | Book catalog |

---

## Run Instructions

### Prerequisites

- Java 17+
- Docker and Docker Compose
- Git

### Run with Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/Nirob2107092/page-carnival.git
cd page-carnival

# Create .env file from the example
cp .env.example .env
# Edit .env with your preferred database credentials

# Build and start the application
docker compose up --build
```

The application will be available at `http://localhost:8080`.

### Run Locally (Without Docker)

```bash
# Ensure PostgreSQL is running locally with a database named page_carnival

# Set environment variables
export DB_URL=jdbc:postgresql://localhost:5432/page_carnival
export DB_USERNAME=postgres
export DB_PASSWORD=your_password

# Build and run
./mvnw clean install
./mvnw spring-boot:run
```

### Run Tests

```bash
./mvnw test
```

Tests use an in-memory H2 database (configured in `src/test/resources/application.properties`) and require no external setup.

---

## Testing

### Unit Tests (Service Layer) -- 19 tests

| Test Class | Tests | What It Covers |
|-----------|-------|----------------|
| `AuthServiceImplTest` | 3 | Registration, duplicate email detection, password encoding |
| `BookServiceImplTest` | 6 | CRUD operations, not-found handling |
| `CartServiceImplTest` | 5 | Add/update/remove items, book-not-found |
| `OrderServiceImplTest` | 5 | Order placement, empty cart rejection, stock validation, status updates |

### Integration Tests (Controller Layer) -- 9 tests

| Test Class | Tests | What It Covers |
|-----------|-------|----------------|
| `ApiBookControllerIntegrationTest` | 3 | GET /api/books (authenticated + unauthenticated), GET /api/books/{id} |
| `ApiCartControllerIntegrationTest` | 3 | GET /api/cart, role-based denial, POST /api/cart/items |
| `AuthControllerIntegrationTest` | 3 | POST /register, GET /register, GET /login |

Integration tests use `@SpringBootTest` + `@AutoConfigureMockMvc` with a real Spring context, security filter chain, and H2 test database.

**Total: 29 tests** (19 unit + 9 integration + 1 context load)

---

## CI/CD Pipeline

The project uses GitHub Actions for continuous integration. The workflow is defined in `.github/workflows/ci.yml`.

### Trigger Events

- Push to `dev` or `feature/*` branches
- Pull requests targeting `dev` or `main`

### Pipeline Steps

1. **Checkout** -- clone the repository
2. **Setup Java 17** -- Temurin distribution with Maven caching
3. **Build** -- `./mvnw clean install -DskipTests`
4. **Test** -- `./mvnw test` (unit + integration tests)
5. **Docker Build** -- `docker build -t page-carnival-app .` (validates the Dockerfile)
6. **Artifact Upload** -- uploads Surefire test reports on failure for debugging

### Branch Strategy

| Branch | Purpose |
|--------|---------|
| `main` | Production-ready code (protected, requires PR + review) |
| `dev` | Development integration branch |
| `feature/*` | Individual feature branches (e.g., `feature/restapi-handling`) |

No direct pushes to `main`. All changes go through pull requests with at least one review approval.

---

## Dockerization

### Dockerfile

Multi-stage build for minimal image size:

- **Stage 1 (Build):** Uses `maven:3.9.9-eclipse-temurin-21` to compile and package the application
- **Stage 2 (Runtime):** Uses `eclipse-temurin:21-jre` (lightweight JRE) to run the JAR

### Docker Compose

Two services orchestrated via `docker-compose.yml`:

| Service | Image | Port | Purpose |
|---------|-------|------|---------|
| `app` | Built from Dockerfile | 8080 | Spring Boot application |
| `postgres` | postgres:15 | 5432 | PostgreSQL database |

All credentials are passed via environment variables from a `.env` file (see `.env.example`). No secrets are hardcoded.

---

## Project Structure

```
page-carnival/
  src/main/java/com/pc/pc/
    config/           -- DataLoader (role seeding)
    controller/       -- 8 controllers (REST API + MVC)
    dto/              -- 8 Data Transfer Objects
    exception/        -- Custom exceptions + global handlers
    model/            -- 5 JPA entities + enums
    repository/       -- 5 Spring Data JPA repositories
    security/         -- SecurityConfig, CustomUserDetails, SuccessHandler
    service/          -- 4 service interfaces + implementations
  src/main/resources/
    templates/        -- Thymeleaf HTML templates
    static/           -- CSS, JS assets
    application.properties
  src/test/
    java/com/pc/pc/
      controller/     -- 3 integration test classes
      service/        -- 4 unit test classes
    resources/
      application.properties  -- H2 test config
  Dockerfile
  docker-compose.yml
  .github/workflows/ci.yml
  .env.example
```
