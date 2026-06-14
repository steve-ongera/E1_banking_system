# E1 Banking System — Java Console Client with Django REST API

A complete banking system featuring a Java console-based client application and a Django REST Framework backend. Users can register, log in, view profiles, and check account balances through an interactive console interface.

---

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Database Seeding](#database-seeding)
- [Usage Guide](#usage-guide)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Features

### Backend (Django REST API)

- User registration with automatic account number generation
- User authentication (login/logout)
- User profile retrieval
- Account balance checking
- SQLite database for easy local setup
- CORS support for cross-origin requests
- Secure password hashing

### Frontend (Java Console Client)

- Interactive menu-driven interface
- User registration and login
- Profile viewing and balance checking
- Clean console UI with ASCII art
- No external dependencies — pure Java implementation

---

## Technology Stack

### Backend

| Component | Version |
|-----------|---------|
| Python | 3.8+ |
| Django | 4.2.0 |
| Django REST Framework | 3.14.0 |
| Database | SQLite (default) |

### Frontend

| Component | Details |
|-----------|---------|
| Java | 11+ |
| HTTP Client | Built-in Java HTTP Client |
| Dependencies | None — pure Java |

---

## Project Structure

```
banking-system/
├── django-backend/
│   ├── banking_api/
│   │   ├── __init__.py
│   │   ├── settings.py
│   │   ├── urls.py
│   │   └── wsgi.py
│   ├── api/
│   │   ├── migrations/
│   │   ├── management/
│   │   │   └── commands/
│   │   │       └── seed_data.py
│   │   ├── __init__.py
│   │   ├── admin.py
│   │   ├── apps.py
│   │   ├── models.py
│   │   ├── serializers.py
│   │   ├── urls.py
│   │   └── views.py
│   ├── db.sqlite3
│   ├── manage.py
│   └── requirements.txt
│
└── java-client/
    └── BankingClient.java
```

---

## Prerequisites

### Backend

- Python 3.8 or higher
- pip

### Frontend

- Java Development Kit (JDK) 11 or higher

---

## Installation and Setup

### Backend Setup (Django)

1. Navigate to the backend directory:

    ```bash
    cd django-backend
    ```

2. Create and activate a virtual environment:

    ```bash
    # Windows
    python -m venv venv
    venv\Scripts\activate

    # Linux / macOS
    python3 -m venv venv
    source venv/bin/activate
    ```

3. Install dependencies:

    ```bash
    pip install -r requirements.txt
    ```

4. Apply database migrations:

    ```bash
    python manage.py makemigrations
    python manage.py migrate
    ```

5. (Optional) Create a superuser for admin access:

    ```bash
    python manage.py createsuperuser
    ```

6. (Optional) Seed the database with sample data:

    ```bash
    python manage.py seed_data
    ```

7. Start the development server:

    ```bash
    python manage.py runserver
    ```

The backend will be available at `http://localhost:8000/`.

### Frontend Setup (Java)

1. Navigate to the client directory:

    ```bash
    cd java-client
    ```

2. Compile the application:

    ```bash
    javac BankingClient.java
    ```

3. Run the application:

    ```bash
    java BankingClient
    ```

No additional dependencies or JAR files are required.

---

## Running the Application

### Step 1 — Start the Backend Server

```bash
cd django-backend
python manage.py runserver
```

Expected output:

```
Watching for file changes with StatReloader
Performing system checks...

System check identified no issues (0 silenced).
Django version 4.2.0, using settings 'banking_api.settings'
Starting development server at http://127.0.0.1:8000/
Quit the server with CTRL-BREAK.
```

### Step 2 — Run the Java Client

Open a new terminal window:

```bash
cd java-client
java BankingClient
```

### Step 3 — Use the Application

Follow the on-screen menu to register, log in, view your profile, and check your balance.

---

## API Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/register/` | Register a new user | `{"username": "john", "email": "john@example.com", "password": "pass123", "phone": "1234567890"}` |
| POST | `/api/login/` | Authenticate a user | `{"username": "john", "password": "pass123"}` |
| GET | `/api/user/{id}/` | Retrieve user details | None |
| GET | `/api/user/{id}/balance/` | Retrieve account balance | None |

### Example Requests

Register a user:

```bash
curl -X POST http://localhost:8000/api/register/ \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com","password":"password123","phone":"1234567890"}'
```

Log in:

```bash
curl -X POST http://localhost:8000/api/login/ \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}'
```

Check balance:

```bash
curl http://localhost:8000/api/user/1/balance/
```

---

## Database Seeding

The project includes a custom management command to populate the database with sample data.

```bash
# Seed with default data (10 predefined users + 20 randomly generated users)
python manage.py seed_data
```

Seeded data includes automatic account numbers (format: `ACCXXXXXXXX`) and random balances between $100 and $50,000.

### Sample Users

| Username | Password | Balance |
|----------|----------|---------|
| john_doe | password123 | $5,000.00 |
| jane_smith | password123 | $7,500.50 |
| mike_johnson | password123 | $3,250.75 |
| sarah_williams | password123 | $10,200.00 |
| robert_brown | password123 | $2,150.25 |

> All seeded users share the password `password123`.

---

## Usage Guide

### Main Menu

```
+----------------------------------------+
|              MAIN MENU                  |
+----------------------------------------+
| 1. Login                                |
| 2. Register New Account                 |
| 3. Exit                                 |
+----------------------------------------+
```

### User Dashboard

```
+----------------------------------------+
|           USER DASHBOARD                |
+----------------------------------------+
| 1. View Profile                         |
| 2. Check Balance                        |
| 3. Logout                               |
+----------------------------------------+
```

### Registering a New Account

1. Select option 2 from the main menu.
2. Enter a unique username, email address, password (minimum 6 characters), and phone number.
3. An account number is generated automatically upon registration.

### Logging In

Select option 1 from the main menu and enter your credentials. On success, the user dashboard is displayed.

---

## Testing

### Using cURL

```bash
# Register
curl -X POST http://localhost:8000/api/register/ \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@test.com","password":"test123","phone":"9999999999"}'

# Login
curl -X POST http://localhost:8000/api/login/ \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"test123"}'
```

### Using Python

```python
import requests

# Register
response = requests.post('http://localhost:8000/api/register/',
    json={'username': 'test', 'email': 'test@test.com', 'password': 'test123', 'phone': '1234567890'})
print(response.json())

# Login
response = requests.post('http://localhost:8000/api/login/',
    json={'username': 'test', 'password': 'test123'})
print(response.json())
```

---

## Troubleshooting

**Java compilation error — "Class names are only accepted if annotation processing is explicitly requested"**

Include the `.java` extension when compiling:

```bash
javac BankingClient.java   # Correct
```

---

**Django server error — "Port 8000 already in use"**

Kill the process on port 8000 or start the server on a different port:

```bash
python manage.py runserver 8001
```

---

**Java client cannot connect to the backend**

Ensure the Django server is running at `http://localhost:8000` and that no firewall is blocking the connection. Verify that `BASE_URL` in `BankingClient.java` matches your server address.

---

**Database migration errors**

Delete the database and migrations, then re-migrate:

```bash
rm db.sqlite3
rm -rf api/migrations/
python manage.py makemigrations api
python manage.py migrate
```

---

**"ModuleNotFoundError: No module named 'rest_framework'"**

Install the required packages:

```bash
pip install djangorestframework django-cors-headers
```

---

**Garbled characters in the Java console**

The client uses ANSI escape codes for color output. If your terminal does not support ANSI codes (e.g., Windows Command Prompt), switch to PowerShell or Git Bash, or remove the color codes from `BankingClient.java`.

---

## Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a Pull Request.

### Guidelines

- Follow PEP 8 for Python code.
- Follow standard Java naming conventions.
- Comment complex logic clearly.
- Update documentation alongside code changes.

---

## Future Enhancements

- Transaction history
- Fund transfers between accounts
- JWT authentication
- Graphical user interface (JavaFX or Swing)
- Email notifications
- Password reset functionality
- PDF account statements
- Two-factor authentication
- Admin dashboard
- Mobile application

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Quick Start

```bash
# Backend
cd django-backend
pip install -r requirements.txt
python manage.py migrate
python manage.py seed_data   # optional
python manage.py runserver

# Frontend (new terminal)
cd java-client
javac BankingClient.java
java BankingClient
```