# LBU Academy Student Portal

Microservices-based Student Portal for Leeds Beckett University
MSc Software Engineering for Service Computing 2025/2026

## Microservices
| Service | Technology | Port |
|---|---|---|
| Student Portal | Spring Boot + MySQL | 8080 |
| Finance Service | Spring Boot + MariaDB (Docker) | 8081 |
| Library Service | Python Flask + MySQL | 8082 |

## Features
- Register / Login (JWT Authentication)
- View Courses
- Enrol in Course (creates Finance invoice + Library account)
- View Enrolments
- View / Update Profile
- Graduation Eligibility (checks Finance balance)

## Tech Stack
- Java 21, Spring Boot 3.5.13
- Spring Security + JWT (jjwt 0.12.6)
- Spring Data JPA + MySQL
- Python Flask + waitress
- Docker + MariaDB
- Frontend: HTML5, CSS3, JavaScript

---

## API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | /api/auth/register | No | Register new student |
| POST | /api/auth/login | No | Login and receive JWT token |
| GET | /api/courses | Yes | View all courses |
| POST | /api/enrolments | Yes | Enrol in a course |
| GET | /api/enrolments | Yes | View my enrolments |
| GET | /api/profile | Yes | View student profile |
| PUT | /api/profile | Yes | Update name |
| GET | /api/graduation | Yes | Check graduation eligibility |

---

## Database Tables

Automatically created by Hibernate on first run:

| Table | Description |
|---|---|
| portal_users | Login credentials |
| student_profiles | Student ID and names |
| courses | Available courses |
| enrolments | Course enrolment records |

---

## Unit Tests

8 unit tests written using JUnit 5 and Mockito.
No database required — all dependencies are mocked.

**PortalUserServiceTest (6 tests)**
- registerNewAccount_Success
- registerAccount_DuplicateUsername_ThrowsException
- registerAccount_DuplicateEmail_ThrowsException
- registerAccount_PasswordIsEncoded
- registerAccount_TokenGenerated
- registerAccount_SaveCalledOnce

**CourseServiceTest (2 tests)**
- getAllCourses_ReturnsAllCourses
- getAllCourses_EmptyList_ReturnsEmpty

**Result: 8/8 tests passing**

---
