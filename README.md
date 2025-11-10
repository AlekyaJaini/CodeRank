# CodeRank
CodeRank is a Coding platform providing REST APIs for problem management and submission evaluation.  
This project is built with Spring Boot, persists data in PostgreSQL, uses Kafka for submission/event flow, and is dockerized for local development.
## Features

- Problem management 
- -Submit code payloads (submissions persisted)
 -Submission processing pipeline (status flow; Kafka integration present)
 -PostgreSQL persistence
  -Docker + Compose setup for local dev (includes Kafka/Zookeeper if enabled in compose)

**  Project Layout**

  CodeRank/
├─ src/main/java/org/example/coderank/controller/ 
├─ src/main/resources/application.properties
├─ docker-compose.yml
├─ Dockerfile
├─ pom.xml
├─ API_SUMMARY.md
└─ README.md  (replace with this file)


## Technologies Used

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Prerequisites
 - Docker & Docker Compose
- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12 or higher

## Configuration


Default config is in src/main/resources/application.properties. Important keys (can be provided as environment variables in Docker or your runtime):

Database:

spring.datasource.url (default in file: jdbc:postgresql://localhost:5432/coderank)

Kafka:

SPRING_KAFKA_BOOTSTRAP_SERVERS (default in properties: kafka:9092)
```

## Building the Application

```bash
mvn clean package
```

## Running the Application

docker-compose up --build

The included docker-compose.yml in the repo configures DB (Postgres) and Kafka/Zookeeper services (if present).

Compose will start the backend application and required infrastructure.

To rebuild after code change:

docker-compose build backend
docker-compose up -d

docker-compose up --build
## API Endpoints

Base URL: http://localhost:8080

#### Get All Problems
```http
GET /problems
```

**Example:**
```bash
curl http://localhost:8080/problems

#### Get Problem by ID
```http
GET /problems/{id}
```

**Example:**
```bash
curl http://localhost:8080/problems/1
```


**Submit Solution**
```http
POST /submissions
Content-Type: application/json
```

**Request Body:**
```json
{
  "problemId": 1,
  "userId": 123,
  "code": "class Solution { public int[] twoSum(int[] nums, int target) { ... } }",
  "language": "Java",
  "status": "PENDING"
}
```


## Database Schema

### Problems Table
- `id` (BIGINT, Primary Key, Auto-increment)
- `title` (VARCHAR, NOT NULL)
- `description` (TEXT, NOT NULL)
- `difficulty` (VARCHAR, NOT NULL)
- `acceptance_rate` (DOUBLE)
- `test_cases` (TEXT)
- `constraints` (TEXT)

### Submissions Table
- `id` (BIGINT, Primary Key, Auto-increment)
- `problem_id` (BIGINT, NOT NULL)
- `user_id` (BIGINT, NOT NULL)
- `code` (TEXT, NOT NULL)
- `language` (VARCHAR, NOT NULL)
- `status` (VARCHAR, NOT NULL)
- `execution_time` (INTEGER)
- `output` (VARCHAR)
- `submitted_at` (TIMESTAMP)

## Architecture

The application follows a layered architecture:

```
├── Controller Layer (REST endpoints)
│   ├── ProblemController
│   └── SubmissionController
├── Service Layer (Business logic)
│   ├── ProblemService
│   └── SubmissionService
├── Repository Layer (Data access)
│   ├── ProblemRepository
│   └── SubmissionRepository
└── Model Layer (Entities)
    ├── Problem
    └── Submission
```

## Future Enhancements
- Test case validation
- Leaderboard and ranking system
- Discussion forum
- Contest management

## License

This project is licensed under the MIT License.
