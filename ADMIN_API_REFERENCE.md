# Admin Management System - API Reference

## Quick Start Guide

### Access Points:
- **Admin Registration:** `http://localhost:8080/admin-register.html`
- **Admin Credentials Access:** `http://localhost:8080/admin-credentials.html`
- **Home Page:** `http://localhost:8080/`

---

## REST API Endpoints

### 1. Register New Admin
**Endpoint:** `POST /api/admin/register`

**Description:** Register a new admin member in the system

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "MySecurePassword123",
  "department": "IT"
}
```

**Success Response:**
- Status: 201 Created
```json
{
  "adminId": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "MySecurePassword123",
  "department": "IT",
  "active": true,
  "createdAt": 1711250639000
}
```

**Error Response:**
- Status: 400 Bad Request
```json
{
  "message": "Username already exists"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/admin/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "password": "MySecurePassword123",
    "department": "IT"
  }'
```

---

### 2. Get Admin by Username
**Endpoint:** `GET /api/admin/credentials/username/{username}`

**Description:** Retrieve admin credentials using username

**URL Parameters:**
- `username` (required): The admin username

**Success Response:**
- Status: 200 OK
```json
{
  "adminId": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "MySecurePassword123",
  "department": "IT",
  "active": true,
  "createdAt": 1711250639000
}
```

**Error Response:**
- Status: 404 Not Found
```json
{
  "message": "Admin not found"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/admin/credentials/username/johndoe
```

---

### 3. Get Admin by Email
**Endpoint:** `GET /api/admin/credentials/email/{email}`

**Description:** Retrieve admin credentials using email address

**URL Parameters:**
- `email` (required): The admin email address

**Success Response:**
- Status: 200 OK
```json
{
  "adminId": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "MySecurePassword123",
  "department": "IT",
  "active": true,
  "createdAt": 1711250639000
}
```

**cURL Example:**
```bash
curl -X GET "http://localhost:8080/api/admin/credentials/email/john.doe@example.com"
```

---

### 4. Get Admin by ID
**Endpoint:** `GET /api/admin/{id}`

**Description:** Retrieve admin credentials using admin ID

**URL Parameters:**
- `id` (required): The admin ID

**Success Response:**
- Status: 200 OK
```json
{
  "adminId": 1,
  "fullName": "John Doe",
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "MySecurePassword123",
  "department": "IT",
  "active": true,
  "createdAt": 1711250639000
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/admin/1
```

---

### 5. Get All Active Admins
**Endpoint:** `GET /api/admin/list/active`

**Description:** Retrieve list of all active admins

**Success Response:**
- Status: 200 OK
```json
[
  {
    "adminId": 1,
    "fullName": "John Doe",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "password": "MySecurePassword123",
    "department": "IT",
    "active": true,
    "createdAt": 1711250639000
  },
  {
    "adminId": 2,
    "fullName": "Jane Smith",
    "username": "janesmith",
    "email": "jane.smith@example.com",
    "password": "AnotherPassword456",
    "department": "CSE",
    "active": true,
    "createdAt": 1711250700000
  }
]
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/admin/list/active
```

---

### 6. Update Admin
**Endpoint:** `PUT /api/admin/{id}`

**Description:** Update admin information

**URL Parameters:**
- `id` (required): The admin ID to update

**Request Body:**
```json
{
  "fullName": "John Doe Updated",
  "username": "johndoe",
  "email": "john.doe.new@example.com",
  "password": "NewPassword123",
  "department": "CSE"
}
```

**Success Response:**
- Status: 200 OK
```json
{
  "adminId": 1,
  "fullName": "John Doe Updated",
  "username": "johndoe",
  "email": "john.doe.new@example.com",
  "password": "NewPassword123",
  "department": "CSE",
  "active": true,
  "createdAt": 1711250639000
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/admin/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe Updated",
    "username": "johndoe",
    "email": "john.doe.new@example.com",
    "password": "NewPassword123",
    "department": "CSE"
  }'
```

---

### 7. Deactivate Admin
**Endpoint:** `DELETE /api/admin/{id}`

**Description:** Deactivate an admin account

**URL Parameters:**
- `id` (required): The admin ID to deactivate

**Success Response:**
- Status: 200 OK
```json
{
  "message": "Admin deactivated successfully"
}
```

**Error Response:**
- Status: 404 Not Found
```json
{
  "message": "Admin not found"
}
```

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/admin/1
```

---

### 8. Activate Admin
**Endpoint:** `POST /api/admin/{id}/activate`

**Description:** Activate a deactivated admin account

**URL Parameters:**
- `id` (required): The admin ID to activate

**Success Response:**
- Status: 200 OK
```json
{
  "message": "Admin activated successfully"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/admin/1/activate
```

---

## HTTP Status Codes

| Code | Description |
|------|-------------|
| 200  | OK - Request successful |
| 201  | Created - Resource successful created |
| 400  | Bad Request - Invalid data or validation error |
| 404  | Not Found - Admin not found |
| 500  | Internal Server Error - Server error |

---

## Validation Rules

### Username
- Required: Yes
- Type: String
- Unique: Yes
- Min Length: 1 character
- Max Length: 255 characters

### Email
- Required: Yes
- Type: String (valid email format)
- Unique: Yes
- Min Length: 6 characters
- Max Length: 255 characters

### Password
- Required: Yes
- Type: String
- Min Length: 6 characters (client-side validation)
- No max length restriction

### Full Name
- Required: Yes
- Type: String
- Min Length: 1 character
- Max Length: 255 characters

### Department
- Required: Yes
- Type: String (must be from predefined list)
- Valid Values:
  - IT (Information Technology)
  - CSE (Computer Science)
  - ECE (Electronics & Communication)
  - ME (Mechanical Engineering)
  - CE (Civil Engineering)
  - Admin (Administration)

---

## CORS Settings

**Allowed Origins:** * (All origins)
**Allowed Headers:** * (All headers)
**Allowed Methods:** GET, POST, PUT, DELETE, etc.

---

## JavaScript Usage Examples

### Register Admin (Fetch API)
```javascript
const formData = {
  fullName: "John Doe",
  username: "johndoe",
  email: "john.doe@example.com",
  password: "MySecurePassword123",
  department: "IT"
};

fetch('/api/admin/register', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(formData)
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

### Get Admin Credentials
```javascript
fetch('/api/admin/credentials/username/johndoe')
  .then(response => response.json())
  .then(data => {
    console.log('Admin credentials:', data);
  })
  .catch(error => console.error('Error:', error));
```

### Get All Active Admins
```javascript
fetch('/api/admin/list/active')
  .then(response => response.json())
  .then(admins => {
    console.log('All active admins:', admins);
    admins.forEach(admin => {
      console.log(`${admin.fullName} (${admin.username}) - ${admin.department}`);
    });
  })
  .catch(error => console.error('Error:', error));
```

---

## Error Handling Examples

### Duplicate Username
```json
{
  "message": "Username already exists"
}
Status: 400 Bad Request
```

### Duplicate Email
```json
{
  "message": "Email already exists"
}
Status: 400 Bad Request
```

### Admin Not Found
```json
{
  "message": "Admin not found"
}
Status: 404 Not Found
```

---

## Testing the API

### Using Postman:
1. Import the endpoints into Postman
2. Set request methods (GET, POST, PUT, DELETE)
3. Add request bodies for POST/PUT requests
4. Test each endpoint

### Using Command Line (cURL):
See cURL examples above for each endpoint

### Using Browser Developer Tools:
Use the Console tab to execute Fetch API calls as shown in JavaScript examples above

---

## Rate Limiting
No rate limiting is currently configured. Consider implementing in production.

## Authentication
No authentication is required for these endpoints. Consider adding JWT or API key authentication in production.

---

**Last Updated:** March 24, 2026
**API Version:** 1.0
