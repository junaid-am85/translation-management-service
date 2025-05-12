# 🈯 Translation API

A Spring Boot 3.3 application (Java 21) that manages and exports localized translations for frontend applications (e.g., Vue.js). It includes authentication, Swagger documentation, and is ready for testing and containerization.

---

## 📦 Features

- CRUD operations on translations (`/api/translations`)
- Export translations as a JSON object
- Filter translations by locale, key, content, and tags
- Bearer Token authentication
- H2 in-memory database support
- Swagger/OpenAPI documentation
- >95% test coverage with unit & integration tests

---

### ✅ Prerequisites

- Java 21
- Maven 3.6+
- (Optional) Docker


### ⚙️ Configuration

- > Defined in src/main/resources/application.yml:


### 📂 API Endpoints
Core Translation Routes

- Method	Endpoint	Description

- GET	`/api/translations/{id}`	Get translation by ID
- POST	`/api/translations`	  Create translation
- PUT	`/api/translations/{id}`	Update translation
- DELETE	`/api/translations/{id}`	Delete translation
- GET	`/api/translations/search`	Search by locale, key, etc.
- GET	`/api/translations/export`	Export as JSON (for frontend)

         
### 🔐 Security

All endpoints are protected using Bearer Token Authentication.

To disable security in tests, security context is mocked in unit tests using `@WithMockUser` or `@Import(SecurityConfig.class)`.
               

### 📊 API Documentation       
http://localhost:8080/v3/api-docs

### 🧪 Testing
```bash
./mvnw test