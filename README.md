# Hospital Backend - Step 1 (Auth)

This is the first step of the Hospital Booking System backend: **User Registration & Login with JWT**.

## How to run
1. Create a MySQL database:
   ```sql
   CREATE DATABASE hospital_booking;
   ```
2. Update `src/main/resources/application.properties` with your MySQL username/password.
3. Build & run:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints
- `POST /auth/register`  
  ```json
  { "name":"Alice", "email":"alice@example.com", "password":"secret123", "role":"PATIENT" }
  ```
- `POST /auth/login`  
  ```json
  { "email":"alice@example.com", "password":"secret123" }
  ```
- `GET /api/me` — Requires `Authorization: Bearer <token>`

## Next Steps
- Add entities for `Doctor`, `Patient`, `Appointment`
- Implement booking endpoints
- Role-based authorization (@PreAuthorize)
