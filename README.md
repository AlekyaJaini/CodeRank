# CodeRank API

A LeetCode-style coding platform with RESTful APIs built using Spring Boot and PostgreSQL.

## Features

- Problem management (CRUD operations)
- Submission tracking and management
- PostgreSQL database integration
- RESTful API endpoints

## Technologies Used

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12 or higher

## Database Setup

1. Install PostgreSQL if not already installed
2. Create a database named `coderank`:
```sql
CREATE DATABASE coderank;
```

3. Update database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/coderank
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Building the Application

```bash
mvn clean package
```

## Running the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/CodeRank-1.0-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Problems API

#### Get All Problems
```http
GET /problems
```

**Query Parameters:**
- `difficulty` (optional): Filter by difficulty level (e.g., Easy, Medium, Hard)
- `search` (optional): Search problems by title

**Example:**
```bash
curl http://localhost:8080/problems
curl http://localhost:8080/problems?difficulty=Easy
curl http://localhost:8080/problems?search=array
```

#### Get Problem by ID
```http
GET /problems/{id}
```

**Example:**
```bash
curl http://localhost:8080/problems/1
```

#### Create Problem
```http
POST /problems
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Two Sum",
  "description": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.",
  "difficulty": "Easy",
  "acceptanceRate": 49.5,
  "testCases": "[{\"input\": \"[2,7,11,15], 9\", \"output\": \"[0,1]\"}]",
  "constraints": "2 <= nums.length <= 10^4"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/problems \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Two Sum",
    "description": "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.",
    "difficulty": "Easy",
    "acceptanceRate": 49.5,
    "testCases": "[{\"input\": \"[2,7,11,15], 9\", \"output\": \"[0,1]\"}]",
    "constraints": "2 <= nums.length <= 10^4"
  }'
```

#### Update Problem
```http
PUT /problems/{id}
Content-Type: application/json
```

#### Delete Problem
```http
DELETE /problems/{id}
```

### Submissions API

#### Submit Solution
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

**Example:**
```bash
curl -X POST http://localhost:8080/submissions \
  -H "Content-Type: application/json" \
  -d '{
    "problemId": 1,
    "userId": 123,
    "code": "class Solution { public int[] twoSum(int[] nums, int target) { return new int[]{0,1}; } }",
    "language": "Java",
    "status": "PENDING"
  }'
```

#### Get All Submissions
```http
GET /submissions
```

**Query Parameters:**
- `userId` (optional): Filter by user ID
- `problemId` (optional): Filter by problem ID
- `status` (optional): Filter by status (e.g., PENDING, ACCEPTED, WRONG_ANSWER)

**Example:**
```bash
curl http://localhost:8080/submissions
curl http://localhost:8080/submissions?userId=123
curl http://localhost:8080/submissions?problemId=1
curl http://localhost:8080/submissions?status=ACCEPTED
```

#### Get Submission by ID
```http
GET /submissions/{id}
```

**Example:**
```bash
curl http://localhost:8080/submissions/1
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

- Code execution engine integration
- User authentication and authorization
- Test case validation
- Leaderboard and ranking system
- Discussion forum
- Contest management

## License

This project is licensed under the MIT License.
