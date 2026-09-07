# 📚 Student Course Management REST API

A RESTful API built with **Spring Boot** for managing **students** and **courses**. It exposes clean, versioned endpoints to create, read, update, partially update, and delete both resources, backed by a PostgreSQL database and validated with Jakarta Bean Validation.

---

## 🚀 Tech Stack

| Category            | Technology                                      |
|----------------------|-------------------------------------------------|
| Language             | Java 21                                          |
| Framework            | Spring Boot 4 (Spring Web MVC, Spring Data JPA)  |
| Database             | PostgreSQL                                       |
| ORM                  | Hibernate (via Spring Data JPA)                  |
| Validation           | Jakarta Bean Validation (`spring-boot-starter-validation`) |
| Boilerplate Reduction| Lombok                                           |
| Build Tool           | Maven                                            |
| Testing              | Spring Boot Test, Spring Data JPA Test           |

---

## 🏗️ Project Structure

The project follows a clean, layered, **package-by-feature** structure:

```
src/main/java/com/example/demo/
├── DemoApplication.java          # Application entry point
├── student/
│   ├── Student.java              # JPA entity
│   ├── StudentRequest.java       # Incoming request DTO (validated)
│   ├── StudentResponse.java      # Outgoing response DTO
│   ├── StudentRepository.java    # Spring Data JPA repository
│   ├── StudentService.java       # Business logic
│   ├── StudentController.java    # REST endpoints
│   └── StudentConfig.java        # Seeds sample data on startup
└── course/
    ├── Course.java                # JPA entity
    ├── CourseRequest.java         # Incoming request DTO (validated)
    ├── CoursePatchRequest.java    # Partial-update DTO (nullable fields)
    ├── CourseResponse.java        # Outgoing response DTO
    ├── CourseRepository.java      # Spring Data JPA repository
    ├── CourseService.java         # Business logic
    ├── CourseController.java      # REST endpoints
    └── CourseConfig.java          # Seeds sample data on startup
```

Each domain (`student`, `course`) is self-contained with its own entity, DTOs, repository, service, and controller — keeping the API contract (DTOs) decoupled from the persistence model (entities).

---

## ✨ Features

- Full **CRUD** support for Students and Courses
- **PUT** (full update) and **PATCH** (partial update) endpoints for both resources
- **Bulk delete** (delete all students / delete all courses)
- **Server-side validation** with descriptive error messages:
  - Student name and email are required and pattern-validated
  - Date of birth must be in the past
  - Course name must be capitalized words; course code must match a `XXXX-000` format (e.g. `JAVA-101`)
- **Duplicate prevention**: rejects a new/updated student with an email already in use, and a new/updated course with a course code already in use
- **Computed field**: a student's `age` is calculated on the fly from their date of birth (not stored)
- **Meaningful HTTP errors** via `ResponseStatusException` (e.g. `404 Not Found`, `400 Bad Request`) with `spring.mvc.problemdetails` enabled for RFC 7807-style error bodies
- **Sample data seeding** on application startup via `CommandLineRunner` beans

---

## ⚙️ Getting Started

### Prerequisites
- Java 21+
- Maven 3.6+
- PostgreSQL running locally (or update the connection settings)

### 1. Configure the database
Create a database named `student` in PostgreSQL, then set your credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 2. Run the application
```bash
./mvnw spring-boot:run
```

The API will be available at:
```
http://localhost:8080
```

---

## 📡 API Endpoints

### 👨‍🎓 Students — Base path: `/api/v1/students`

| Method | Endpoint                     | Description              |
|--------|-------------------------------|---------------------------|
| GET    | `/api/v1/students`            | Get all students          |
| POST   | `/api/v1/students`             | Register a new student    |
| PUT    | `/api/v1/students/{studentID}` | Fully update a student    |
| PATCH  | `/api/v1/students/{studentID}` | Partially update a student|
| DELETE | `/api/v1/students/{studentID}` | Delete one student        |
| DELETE | `/api/v1/students`             | Delete all students       |

**Student request body** (`POST` / `PUT` / `PATCH`):
```json
{
  "name": "Hamdy Tamer",
  "email": "hamdytamer253@gmail.com",
  "dob": "2004-08-29"
}
```

**Student response body:**
```json
{
  "id": 1,
  "name": "Hamdy Tamer",
  "email": "hamdytamer253@gmail.com",
  "dob": "2004-08-29",
  "age": 22
}
```

**Validation rules:**
- `name`: required, capitalized (e.g. `Hamdy Tamer`), 3–15 characters
- `email`: required, must be a valid email format
- `dob`: required, must be a date in the past

---

### 📘 Courses — Base path: `/api/v1/courses`

| Method | Endpoint                     | Description             |
|--------|-------------------------------|--------------------------|
| GET    | `/api/v1/courses`              | Get all courses          |
| POST   | `/api/v1/courses`               | Register a new course    |
| PUT    | `/api/v1/courses/{courseID}`    | Fully update a course    |
| PATCH  | `/api/v1/courses/{courseID}`    | Partially update a course|
| DELETE | `/api/v1/courses/{courseID}`    | Delete one course        |
| DELETE | `/api/v1/courses`                | Delete all courses       |

**Course request body** (`POST` / `PUT`):
```json
{
  "course_name": "Java Backend Programming",
  "course_code": "JAVA-101"
}
```

**Course PATCH body** (all fields optional):
```json
{
  "course_name": "Advanced Java Backend Programming"
}
```

**Course response body:**
```json
{
  "courseID": 1,
  "course_name": "Java Backend Programming",
  "course_code": "JAVA-101"
}
```

**Validation rules:**
- `course_name`: required, capitalized words (e.g. `Java Backend Programming`)
- `course_code`: required, must match the format `XXXX-000` (e.g. `JAVA-101`)

---

## 🧪 Testing the API in Postman

Below are ready-to-use requests for each endpoint. Set a Postman **collection variable** `base_url = http://localhost:8080` so you can reuse it across requests.

### Students

| # | Method | URL                                          | Body |
|---|--------|-----------------------------------------------|------|
| 1 | GET    | `{{base_url}}/api/v1/students`                | — |
| 2 | POST   | `{{base_url}}/api/v1/students`                | `{"name": "Farah Ahmed", "email": "farahahmed690@gmail.com", "dob": "2001-05-07"}` |
| 3 | PUT    | `{{base_url}}/api/v1/students/1`              | `{"name": "Farah Adel", "email": "farahahmed690@gmail.com", "dob": "2001-05-07"}` |
| 4 | PATCH  | `{{base_url}}/api/v1/students/1`              | `{"name": "Farah Adel"}` |
| 5 | DELETE | `{{base_url}}/api/v1/students/1`              | — |
| 6 | DELETE | `{{base_url}}/api/v1/students`                | — |

### Courses

| # | Method | URL                                        | Body |
|---|--------|----------------------------------------------|------|
| 1 | GET    | `{{base_url}}/api/v1/courses`                 | — |
| 2 | POST   | `{{base_url}}/api/v1/courses`                  | `{"course_name": "Political Science", "course_code": "POLT-102"}` |
| 3 | PUT    | `{{base_url}}/api/v1/courses/1`                | `{"course_name": "Java Backend Programming", "course_code": "JAVA-101"}` |
| 4 | PATCH  | `{{base_url}}/api/v1/courses/1`                | `{"course_code": "JAVA-102"}` |
| 5 | DELETE | `{{base_url}}/api/v1/courses/1`                | — |
| 6 | DELETE | `{{base_url}}/api/v1/courses`                  | — |

**Postman setup notes:**
- For `POST`, `PUT`, and `PATCH` requests, go to the **Body** tab → select **raw** → choose **JSON** from the dropdown, then paste the request body.
- Make sure the `Content-Type: application/json` header is set (Postman does this automatically when you select the JSON body type).
- Successful `POST` requests return `201 Created` with the created resource.
- Invalid input returns `400 Bad Request` with a validation error message; requesting a non-existent ID returns `404 Not Found`.

---

## 📄 License

This project is open source. Feel free to fork, modify, and use it for learning purposes.
