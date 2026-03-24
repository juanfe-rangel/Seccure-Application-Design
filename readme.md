# Secure Application Design

A secure web application deployed on AWS with two independent servers: Apache serves the asynchronous HTML+JS client over HTTPS with a Let's Encrypt certificate, and Spring Boot exposes a REST API protected with TLS and Basic Auth authentication with passwords hashed using BCrypt. Spring runs in a Docker container.

---

## Architecture

**Project Structure**
```
/
lab-8
│   ├── Dockerfile                        → Spring Boot Docker Image
│   ├── pom.xml
│   └── keystores
│       ├── ecicert.cer
│       ├── myTrustStore         
│       └── ecikeystore.p12      
│   └──src/main/java/com/eci/example/
│       ├── util/
│       │   ├── User.java  
│       │   ├── UserInitializer.java  
│       │   └── UserRepository.java                      
│       ├── controllers/
│       │   ├── AuthController.java       → POST /api/register · GET /api/me
│       │   └── HelloController.java      → GET / (protected)
│       │   └── PiController.java         → GET /pi (protected)
│       └── SecurityConfig.java   
│   └── src/main/resources/
│       ├── application.properties
└── README.md
```

### Application Flow
```
User Browser
    │
    │  HTTPS port 443 — Let's Encrypt certificate
    ▼
EC2 #1 — Apache HTTP Server (directly on EC2)
Serves index.html to browser
    │
    │  HTTPS port 8443 — PKCS12 certificate (keytool)
    │  Authorization: Basic base64(username:password)
    ▼
EC2 #2 — Docker Container (Spring Boot)
Validates credentials → BCrypt → returns JSON
```

---

## Requirements

- **Java 17**
- **Maven 3.6** or higher
- **Docker**
- **AWS Account** with two EC2 instances (Amazon Linux 2023)
- **Domain** pointing to Apache EC2

Verify installation:
```bash
java -version
mvn -version
docker -v
```

---

## Local Installation and Execution

### 1. Clone the repository
```bash
git clone https://github.com/SantiagoSu15/Secure-App.git
```

### 2. Generate keystore for Spring
```bash
# Generate PKCS12 key pair
keytool -genkeypair -alias ecikeypair -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore ecikeystore.p12   -validity 3650

# Export certificate
keytool -export  -keystore ./ecikeystore.p12  -alias ecikeypair -file ecicert.cer

# Import to TrustStore
keytool -import   -file ./ecicert.cer   -alias firstCA -keystore myTrustStore
```

### 3. Compile and build Docker image
```bash
cd spring-app
mvn clean package
docker build -t secure-app .
```

### 4. Start the container
```bash
docker compose up
```

The API is available at `https://localhost:8443`

---

## AWS Deployment

### Apache Server

Verify that Apache server is running
![alt text](image.png)

**Certificate with Let's Encrypt:**

![img](/docs/img_11.png)

**Supporting HTTPS with Let's Encrypt:**

![img](/docs/img_13.png)

**Upload HTML to server:**

Transfer index.html to the EC2 instance
```bash
sftp key instance@instance_ip

put index.html
```

Transfer file from EC2 instance to specific folder

```bash
sudo cp index.html /var/www/html/index.html
```

Configured with Duck DNS

<img width="1595" height="239" alt="image" src="https://github.com/user-attachments/assets/faf0d7a5-1325-40ca-9efa-78056f7d61de" />


---

### Spring Boot on Docker

#### Push image to Docker Hub

**Check local images:**
```bash
docker images
```

![img](/docs/img.png)

**Add tag for Docker Hub and push to repository:**

![alt text](docs/image-2.png)

#### Deploy on EC2

**Run the container:**

![alt text](docs/image-3.png)

---

### Certificate

![alt text](image-1.png)

### Test from browser

![alt text](image-2.png)

---

## Implemented Security

| Feature | Implementation |
|---|---|
| TLS on Apache | Let's Encrypt certificate via Certbot |
| TLS on Spring | PKCS12 Keystore generated with keytool |
| Authentication | HTTP Basic Auth — Spring Security |
| Password Storage | BCrypt with work factor 12 |
| CORS | Restricted to Apache domain |
| Sessions | Stateless — credentials encrypted per request |

Test users created automatically on startup:

| User | Password | Role |
|---|---|---|
| `admin` | `Admin2025!` | ROLE_ADMIN |
| `estudiante` | `Eci12345!` | ROLE_USER |

---
## Built with

- **Java 17** — Main language
- **Spring Boot 3.2** — Backend framework
- **Spring Security** — Authentication and BCrypt
- **Apache httpd** — Web server for client
- **Let's Encrypt / Certbot** — TLS for Apache
- **Java keytool** — PKCS12 Keystore for Spring
- **Docker** — Spring Boot container
- **Docker Hub** — Image registry
- **AWS EC2** — Cloud infrastructure

---

## Author

juan felipe rangel rodriguez
