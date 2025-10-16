# FlexLink Shortening Service

A simple URL shortening service. Users can shorten URLs and get redirected to the original URLs.

---

## Technologies & Libraries

### Backend

- Spring Boot 3.5.6
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database (in-memory)
- Lombok
- Maven

### Frontend

- React 19
- React Router DOM 7
- Axios
- Vite
- TypeScript
- Bootstrap 5

---

## Prerequisites

- Java 21+
- Node.js 18+
- Maven 3+

---

## Backend Setup

1. Clone the repository:

```console
git clone https://github.com/pawelmieczkowski/LinkShortening.git
cd flexlink-shortening/backend
```

2. Build and run the backend:

```console
mvn clean install
mvn spring-boot:run
```

3. The backend by default runs at: http://localhost:8082

## Frontend Setup

1. Navigate to the frontend folder:

```console
cd ../frontend
```

2. Install dependencies:

```console
npm install
```

3. Run the development server:

```console
npm run dev
```

4. The frontend is available at: http://localhost:5173.

## API Documentation (OpenAPI JSON)

You can download the API definition [here](./api-docs.json)
