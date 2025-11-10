# API Endpoints Summary

## Base URL
`http://localhost:8080`

## Problems API

### 1. GET /problems
Retrieve all problems (with optional filtering)

**Query Parameters:**
- `difficulty` (optional): Filter by difficulty (e.g., Easy, Medium, Hard)
- `search` (optional): Search by title

**Response:** Array of Problem objects

### 2. GET /problems/{id}
Get a specific problem by ID

**Response:** Problem object or 404 if not found

### 3. POST /problems
Create a new problem

**Request Body:** Problem object
**Response:** Created Problem object (201 status)

### 4. PUT /problems/{id}
Update an existing problem

**Request Body:** Problem object
**Response:** Updated Problem object or 404 if not found

### 5. DELETE /problems/{id}
Delete a problem

**Response:** 204 No Content

## Submissions API

### 1. POST /submissions
Submit a solution (Main requirement)

**Request Body:** Submission object
**Response:** Created Submission object (201 status)

**Required Fields:**
- problemId (Long)
- userId (Long)
- code (String)
- language (String)

### 2. GET /submissions
Retrieve all submissions (with optional filtering)

**Query Parameters:**
- `userId` (optional): Filter by user ID
- `problemId` (optional): Filter by problem ID
- `status` (optional): Filter by status

**Response:** Array of Submission objects

### 3. GET /submissions/{id}
Get a specific submission by ID

**Response:** Submission object or 404 if not found

## Data Models

### Problem
```json
{
  "id": 1,
  "title": "Two Sum",
  "description": "Problem description",
  "difficulty": "Easy",
  "acceptanceRate": 49.5,
  "testCases": "Test cases JSON",
  "constraints": "Problem constraints"
}
```

### Submission
```json
{
  "id": 1,
  "problemId": 1,
  "userId": 123,
  "code": "Solution code",
  "language": "Java",
  "status": "PENDING",
  "executionTime": 100,
  "output": "Output result",
  "submittedAt": "2024-01-01T12:00:00"
}
```

## Validation Rules

### Problem
- `title`: Required, not blank
- `description`: Required, not blank
- `difficulty`: Required, not blank

### Submission
- `problemId`: Required, not null
- `userId`: Required, not null
- `code`: Required, not blank
- `language`: Required, not blank
