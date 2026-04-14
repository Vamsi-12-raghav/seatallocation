# Deployment Guide

This project can be deployed in two ways:

## Option 1: Full Stack on Render (Recommended)
Backend (Java Spring Boot) + Frontend served from same origin

### Deploy to Render
1. Push to GitHub
2. Go to [render.com](https://render.com)
3. Create New → Web Service
4. Connect your GitHub repository
5. Settings:
   - Runtime: Docker
   - Build Command: `cd seatallocation && ./mvnw clean package`
   - Start Command: `cd seatallocation && java -jar target/seatallocation-0.0.1-SNAPSHOT.jar`
6. Deploy

**Result:** Frontend accessible at `https://<app>.onrender.com`

---

## Option 2: Separated Deployment

### Backend on Render
1. Deploy `seatallocation/` folder to Render (see render.yaml)
2. Note the backend URL (e.g., `https://seatallocation.onrender.com`)

### Frontend on Vercel
1. Go to [vercel.com](https://vercel.com)
2. Create New Project from `frontend-vercel/` folder
3. Add Environment Variable:
   - Key: `REACT_APP_API_URL`
   - Value: `https://seatallocation.onrender.com` (your Render backend)
4. Deploy

**Result:** 
- Frontend on Vercel: `https://<project>.vercel.app`
- Backend on Render: `https://seatallocation.onrender.com`

---

## Environment Variables Needed

### On Render (Backend)
```
PORT=8080
JDBC_DATABASE_URL=postgres://...
JDBC_DATABASE_USERNAME=...
JDBC_DATABASE_PASSWORD=...
OPENAI_API_KEY=your-api-key (optional)
```

### On Vercel (Frontend)
```
REACT_APP_API_URL=https://seatallocation.onrender.com
```

---

## Local Development

### Run Both Services Locally
```bash
# Terminal 1: Backend (from seatallocation folder)
./mvnw spring-boot:run

# Terminal 2: Frontend (from frontend-vercel folder)
cd frontend-vercel
python -m http.server 3000
```

Access at:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- API: http://localhost:8080/api

---

## Default Admin Credentials
- Username: `vamsi`
- Password: `vamsi2005`
