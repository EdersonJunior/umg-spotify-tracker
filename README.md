## Technologies Used

### Backend (Java)
- Java 17
- Spring Boot
- Spring Security (Basic Auth)
- Spring Data JPA
- H2 Database (in-memory)
- SpringDoc OpenAPI (Swagger UI)
- RestTemplate
- Lombok

### Frontend (React)
- React (with Vite)
- Fetch API
- Basic HTML + CSS
- Authorization via headers

---

## Run the Backend

1. mvn clean install
2. mvn spring-boot:run
3. Swagger URL: http://localhost:8080/swagger-ui.html

---

# Run the frontend

1. npm install
2. npm run dev
3. Open the http://localhost:5173 URL

---

---

## Authentication

All endpoints are protected with basic auth:

- Username: `admin`
- Password: `admin`
