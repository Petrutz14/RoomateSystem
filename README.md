# Roommate System

A Spring Boot REST API for managing shared expenses between roommates. Track apartments, expenses, and payments all in one place.

## What it does

This is a backend service that helps roommates split bills and track who's paid what. You can create apartments, add expenses (like rent, utilities, groceries), and record payments. The API uses JWT authentication so each user only sees their own data.

## Tech stack

- **Spring Boot 4.0.1** - Main framework
- **PostgreSQL** - Database
- **Spring Security** - Authentication and authorization
- **JWT** - Token-based auth
- **Maven** - Dependency management

## Getting started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL (or use Docker Compose)

### Setup

1. Clone the repo:
```bash
git clone <your-repo-url>
cd p3project
```

2. Start PostgreSQL with Docker Compose:
```bash
docker-compose up -d
```

Or set up PostgreSQL manually and update `application.properties` with your database credentials.

3. The database schema will be created automatically from `src/main/resources/schema.sql` when you first run the app.

4. Run the application:
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

## API endpoints

### Authentication

- `POST /api/users/register` - Create a new account
- `POST /api/users/login` - Login and get a JWT token

### Apartments

- `GET /api/apartments` - Get all apartments for the current user
- `GET /api/apartments/{id}` - Get apartment by ID
- `POST /api/apartments` - Create a new apartment (requires auth)
- `PUT /api/apartments/{id}` - Update apartment (requires auth)
- `DELETE /api/apartments/{id}` - Delete apartment (requires auth)
- `POST /api/apartments/{apartmentId}/members/{userId}` - Add a member to an apartment

### Expenses

- `GET /api/expenses/apartment/{apartmentId}` - Get all expenses for an apartment
- `POST /api/expenses` - Create a new expense (requires auth)

### Payments

- `GET /api/payments/expense/{expenseId}` - Get all payments for an expense
- `POST /api/payments` - Record a payment (requires auth)

### Admin

- `GET /api/admin/users` - Get all users (admin only)
- `DELETE /api/admin/users/{id}` - Delete a user (admin only)

## Authentication

Most endpoints require a JWT token. After logging in, you'll get a token that you need to include in the `Authorization` header:

```
Authorization: Bearer <your-token-here>
```

Tokens expire after 24 hours by default (configurable in `application.properties`).

## Configuration

Edit `src/main/resources/application.properties` to change:

- Database connection details
- JWT secret and expiration time
- Connection pool settings

## Database schema

The app uses these main tables:
- `users` - User accounts
- `apartments` - Apartment listings
- `apartment_members` - Links users to apartments (many-to-many)
- `expenses` - Bills/expenses tied to apartments
- `payments` - Individual payments toward expenses

## Testing

There's a test file at `src/main/resources/api.http` with example requests you can use with REST Client extensions in VS Code or IntelliJ.

## Notes

- Passwords are hashed using Spring Security's BCrypt
- Users can only access apartments they're members of
- Expenses are tied to apartments, not individual users
- The first user you create will have the USER role by default
