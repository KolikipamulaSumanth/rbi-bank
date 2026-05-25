# RBI Bank

RBI Bank is a simple banking project. It has a backend for bank data and a frontend for the user screens.

The project can be used to practice how a banking app works, such as creating users, managing accounts, cards, transactions, and service requests.

## Main Features

- User registration and login
- Bank account details
- Card details
- Money transfer
- Transaction history
- Service requests
- Simple employee/admin side APIs

## Project Folders

- `backend` contains the Spring Boot application.
- `frontend` contains the React application.
- `docker-compose.yml` starts the backend and MySQL database together.

## How To Run With Docker

First, open Docker Desktop.

Then run this command from the main project folder:

```bash
docker compose up -d --build
```

The backend will run at:

```text
http://localhost:8080/api/v1
```

To stop the project, run:

```bash
docker compose down
```

## How To Run The Frontend

Go to the frontend folder:

```bash
cd frontend
```

Install packages:

```bash
npm install
```

Start the frontend:

```bash
npm run dev
```

The frontend usually runs at:

```text
http://localhost:5173
```

## Backend Test Command

Go to the backend folder:

```bash
cd backend
```

Run tests:

```bash
./mvnw test
```

On Windows PowerShell, use:

```bash
.\mvnw.cmd test
```

## Notes

- Docker is the easiest way to run the backend because it also starts the database.
- Make sure port `8080` is free before starting the backend.
- This project is for learning and practice.
