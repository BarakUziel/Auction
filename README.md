## Running the Application

You can run the application in two ways: using Docker or manually.

---

### Option 1: Run with Docker

1. From the root of the `Auction` project, run:

```bash
docker-compose up --build -d
```

This will initialize the database and start the server 

2. Start the Client

From the root of the `client` folder, run:

```bash
./gradlew run
```

---

### Option 2: Run Manually (without Docker)

1. Initialize the Database

Make sure MySQL is installed and running locally, then execute:

```bash
mysql -u root -p < sql/init.sql
```

2. Start the Server

From the root of the `server` folder, run:

```bash
./gradlew bootRun
```

3. Start the Client

From the root of the `client` folder, run:

```bash
./gradlew run
```