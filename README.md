# Task Management System

A robust Spring Boot-based Task Management System with role-based access, JWT authentication, filtering, and pagination. This application allows admins and users to manage tasks efficiently and provides API documentation via Swagger.

---

## Features

### General

- **JWT-based authentication** and **role-based authorization**.

- **Database integration** with PostgreSQL.


### Admin Features

- Create, update, delete, assign, and filter tasks.

- View all tasks with optional pagination and filtering.


### User Features

- View and manage their assigned tasks.

- Add comments to tasks.


### Documentation

- Swagger UI for API documentation.


---

## Requirements

Before you begin, ensure you have the following installed:

- **Java 17** or higher.

- **Gradle** for building the application.

- **Docker** and **Docker Compose** for containerized deployment.

- **PostgreSQL** for local database setup.


---

## Running the Application

### Running Locally

1. Clone the repository:

    ```
    git clone https://github.com/nurlantr/task-management-system.git
    cd task-management-system
    ```

2. Set up the PostgreSQL database:

   - Create a database named `taskdb`.

   - Use the following credentials:

      - Username: `nurlantr`

      - Password: `12345`

3. Configure `application.yml`: Ensure your `spring.datasource.url`, username, and password match the local PostgreSQL setup.

4. Build and run the application:

    ```
    ./gradlew clean build
    java -jar build/libs/task-management-system-0.0.1-SNAPSHOT.jar
    ```

5. Access the application:

   - **Swagger UI**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)


---

### Running with Docker Compose

1. Clone the repository:

    ```
    git clone https://github.com/nurlantr/task-management-system.git
    cd task-management-system
    ```

2. Build and run the application with Docker Compose:

    ```
    docker-compose up --build
    ```

3. Access the application:

   - **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

4. Stop the application:

    ```
    docker-compose down
    ```


---

## API Documentation

### Swagger UI

Access the API documentation at:

- **Swagger UI - Localhost** (local setup)

- **Swagger UI - Docker** (Docker Compose)



| HTTP Method | Endpoint                          | Description                                            |
| ----------- | --------------------------------- | ------------------------------------------------------ |
| **POST**    | `/api/v1/auth/register`           | Register a new user or admin.                          |
| **POST**    | `/api/v1/auth/authenticate`       | Authenticate and retrieve a JWT token.                 |
| **POST**    | `/api/v1/admin/tasks/create`      | Create a new task (admin only).                        |
| **PUT**     | `/api/v1/admin/tasks/{id}`        | Update an existing task by ID (admin only).            |
| **PUT**     | `/api/v1/admin/tasks/{id}/assign` | Assign an executor to a task by ID (admin only).       |
| **DELETE**  | `/api/v1/admin/tasks/delete/{id}` | Delete a task by ID (admin only).                      |
| **GET**     | `/api/v1/tasks/all`               | Retrieve all tasks (admins and users).                 |
| **GET**     | `/api/v1/tasks`                   | Retrieve tasks with optional filtering and pagination. |
| **GET**     | `/api/v1/tasks/{id}/comments`     | Retrieve all comments for a specific task.             |
| **PUT**     | `/api/v1/user/tasks/{id}/status`  | Update the status of a task assigned to the user.      |
| **POST**    | `/api/v1/user/tasks/{id}/comment` | Add a comment to a task assigned to the user.          |

---

## Development Notes

### Environment Variables

The following environment variables are used in the Docker Compose setup:

- `SPRING_DATASOURCE_URL`

- `SPRING_DATASOURCE_USERNAME`

- `SPRING_DATASOURCE_PASSWORD`


Make sure to update these values in `docker-compose.yml` if you change the database configuration.

---

### Testing

To run unit tests:

```
./gradlew test
```