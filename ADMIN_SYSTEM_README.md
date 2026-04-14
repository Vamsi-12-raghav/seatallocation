# Admin Management System - Documentation

## Overview
A new admin management system has been added to the Seat Allocation Application, allowing admin members to self-register and access their login credentials securely.

## Features

### 1. **Admin Registration**
- **URL:** `http://localhost:8080/admin-register.html`
- **Purpose:** New admin members can register themselves with the system
- **Required Fields:**
  - Full Name
  - Username (must be unique)
  - Email (must be unique and valid)
  - Password (minimum 6 characters)
  - Confirm Password
  - Department (dropdown selection)

- **Departments Available:**
  - Information Technology
  - Computer Science
  - Electronics & Communication
  - Mechanical Engineering
  - Civil Engineering
  - Administration

- **Process:**
  1. Navigate to the registration page
  2. Fill in all required fields
  3. Confirm password matches
  4. Click "Register Admin"
  5. Credentials will be displayed upon successful registration
  6. Save credentials securely

### 2. **Admin Credentials Access**
- **URL:** `http://localhost:8080/admin-credentials.html`
- **Purpose:** Retrieve admin login credentials using various search methods

#### Search Methods:
- **By Username:** Search for an admin using their username
- **By Email:** Search for an admin using their email address
- **List All:** View all active admin accounts

#### Credential Display:
- Admin ID
- Full Name
- Username (with copy button)
- Password (masked, with copy button)
- Email (with copy button)
- Department
- Status (Active/Inactive)
- Creation Date & Time

#### Security Features:
- Passwords are masked in the list view
- Copy-to-clipboard functionality for easy credential management
- One-click copy for sensitive information
- Security warning displayed on the page

## API Endpoints

### Base URL: `/api/admin`

#### 1. Register New Admin
```
POST /api/admin/register
```
**Request Body:**
```json
{
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123",
  "department": "IT"
}
```
**Response:** 
- Status: 201 Created
- Returns admin credentials with generated Admin ID

#### 2. Get Credentials by Username
```
GET /api/admin/credentials/username/{username}
```
**Response:** Admin credential object

#### 3. Get Credentials by Email
```
GET /api/admin/credentials/email/{email}
```
**Response:** Admin credential object

#### 4. Get Admin by ID
```
GET /api/admin/{id}
```
**Response:** Admin credential object

#### 5. Get All Active Admins
```
GET /api/admin/list/active
```
**Response:** Array of active admin credentials

#### 6. Update Admin
```
PUT /api/admin/{id}
```
**Request Body:** Same as registration request
**Response:** Updated admin credential object

#### 7. Deactivate Admin
```
DELETE /api/admin/{id}
```
**Response:** Success message

#### 8. Activate Admin
```
POST /api/admin/{id}/activate
```
**Response:** Success message

## Database Schema

### Admin Table
```sql
CREATE TABLE admin (
  admin_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  full_name VARCHAR(255) NOT NULL,
  department VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at BIGINT NOT NULL
);
```

## File Structure

### Backend (Java)
```
src/main/java/com/example/seatallocation/
├── entity/
│   └── Admin.java
├── repository/
│   └── AdminRepository.java
├── service/
│   └── AdminService.java
├── controller/
│   └── AdminController.java
├── dto/
│   ├── AdminRegistrationRequest.java
│   └── AdminCredentialResponse.java
```

### Frontend (HTML/JavaScript)
```
src/main/resources/static/
├── admin-register.html
├── admin-register.js
├── admin-credentials.html
├── admin-credentials.js
└── index.html (updated)
```

## How to Use

### For New Admins

1. **Register:**
   - Open `http://localhost:8080` (or click "Register New Admin" button)
   - Fill in all required information
   - Save the credentials displayed after successful registration

2. **Access Credentials Later:**
   - Open `http://localhost:8080` (or click "Access Admin Credentials" button)
   - Choose search method (Username, Email, or List All)
   - Enter search criteria
   - Credentials will be displayed with options to copy

### For System Administrators

1. **View All Admins:**
   - Navigate to credentials page
   - Click "Load All Active Admins"
   - View complete list of all active administrators

2. **Manage Admin Status:**
   - Use the API endpoints to activate/deactivate admins
   - Use dashboard to manage access control

## Security Considerations

⚠️ **Important:**
1. **Password Security:**
   - Passwords are stored as plain text in the database (consider implementing hashing in production)
   - Passwords should be at least 6 characters long
   - Each admin should use a unique, strong password

2. **Email Validation:**
   - Valid email format is required
   - Email addresses must be unique per admin

3. **Username Uniqueness:**
   - Usernames must be unique
   - Usernames are used for login

4. **Data Protection:**
   - Keep credentials confidential
   - Do not share with unauthorized personnel
   - Credentials page shows security warning

## Future Enhancements

1. **Password Hashing:** Implement bcrypt or similar for password encryption
2. **Password Reset:** Email-based password recovery
3. **Admin Roles:** Implement role-based access control (Super Admin, Admin, Moderator)
4. **Audit Logging:** Track all admin activities
5. **Two-Factor Authentication:** Add 2FA for enhanced security
6. **Session Management:** Implement session timeout and management
7. **Admin Dashboard:** Comprehensive admin control panel

## Running the Application

```bash
cd seatallocation
./mvnw.cmd spring-boot:run
```

Access the application at: `http://localhost:8080`

## Troubleshooting

### Issue: "Username already exists"
- **Solution:** Choose a different username. Usernames must be unique.

### Issue: "Email already exists"
- **Solution:** Use a different email address. Emails must be unique.

### Issue: "Password mismatch"
- **Solution:** Ensure both password fields match exactly.

### Issue: Credentials not found
- **Solution:** Verify the search criteria (correct username, email, or valid admin ID).

### Issue: Cannot register admin
- **Solution:** Ensure all fields are filled correctly and follow validation rules.

## Support
For issues or questions, please contact the system administrator or development team.
