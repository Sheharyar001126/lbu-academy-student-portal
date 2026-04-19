# LBU Academy Student Portal

Microservices-based web application for Leeds Beckett University.

## Project Structure
| Microservice | Technology | Contributor |
|---|---|---|
| Student Portal (this repo) | Spring Boot, MySQL | Shehryar |
| Finance Microservice | Spring Boot, MariaDB | Teammate 2 |
| Library Microservice | Python Flask, MySQL | Teammate 3 |

## Student Portal Features
- Register / Login (JWT Auth)
- View Courses
- Enrol in Course
- View Enrolments
- View / Update Profile
- Graduation Eligibility Check

## Tech Stack
- Java 21, Spring Boot 3.5.13
- Spring Security + JWT (jjwt 0.12.6)
- Spring Data JPA + MySQL
- Frontend: HTML, CSS, JavaScript

## Run Instructions
1. Create MySQL database: `academy_portal`
2. Update `application.properties` with your MySQL password
3. Run `StudentPortalApplication`
4. Open `frontend-app/index.html` with Live Server

## Ports
- Student Portal: 8080
- Finance Service: 8081
- Library Service: 8082
