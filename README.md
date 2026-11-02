# IT Asset & Equipment Tracker — Backend

A REST API for managing an organization's IT assets, equipment, and employee assignments.

## Technologies

- Java 21
- Spring Boot 3
- Spring Data JPA / Hibernate
- PostgreSQL
- Spring Security
- JSON Web Tokens (JWT)
- Maven
- Docker and Docker Compose
- Swagger / OpenAPI

## Main Features

- User registration and login
- JWT-based authentication
- Role-based access control (Admin and Staff)
- Create and manage IT assets
- Manage asset categories
- Assign assets to employees
- Return assigned assets
- View assets assigned to the authenticated staff member

## User Roles

### Administrator
- Create and manage assets.
- Create and manage categories.
- Assign assets to staff members.
- Manage administrative operations.

### Staff
- Log in securely.
- View their assigned assets.
- Return assigned assets.

## Running the Project

### Requirements

- Java 21
- Docker Desktop
- Git

### Start the application

1. Clone the repository.
2. Create a local `.env` file using the required environment variables.
3. Start Docker Desktop.
4. Open PowerShell in the project folder.
5. Run:

```powershell
docker compose up -d --build
```

Check the containers:

```powershell
docker compose ps
```

### API Documentation

When the application is running, open:

http://localhost:8081/swagger-ui/index.html

## Main API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Authenticate and receive a JWT |
| GET | `/api/assets` | View assets |
| POST | `/api/assets` | Create an asset |
| POST | `/api/assets/{id}/assign/{userId}` | Assign an asset |
| PATCH | `/api/assets/{id}/return` | Return an asset |
| GET | `/api/assets/my-assets` | View my assigned assets |
| GET | `/api/categories` | View categories |
| GET | `/api/users` | View users |

*Access to endpoints depends on authentication and user role.*

## Security

The API uses JWT authentication and role-based authorization to protect resources.

**Important:** Never commit `.env` files, passwords, database credentials, or JWT secrets to GitHub.

## Repository

https://github.com/selamawit23-13/it-asset-tracker-backend