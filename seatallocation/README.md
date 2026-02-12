# Smart Exam Seating Allocation System

## Project Structure
```
seatallocation/
  database/
    schema.sql
    sample-data.sql
  frontend/
    index.html
    styles.css
    app.js
  src/
    main/
      java/com/example/seatallocation/
        config/
        controller/
        dto/
        entity/
        repository/
        service/
      resources/
        application.properties
    test/
      java/com/example/seatallocation/
```

## Backend Setup
1. Update MySQL credentials in `src/main/resources/application.properties`.
2. Run `database/schema.sql` and `database/sample-data.sql` if you want sample data.
3. Start the Spring Boot app.

## Frontend Setup
1. Open `frontend/index.html` in a browser.
2. Ensure the backend runs at `http://localhost:8080`.

## Admin Login
Default credentials are stored in application properties:
- Username: `admin`
- Password: `admin123`
