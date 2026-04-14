# Frontend Deployment on Vercel

## Structure
```
frontend-vercel/
├── package.json
├── vercel.json
├── index.html
├── styles.css
├── config.js
├── login.js
└── other HTML/JS files
```

## Deployment Steps

### 1. Create New Vercel Project (Frontend)
```bash
cd frontend-vercel
vercel
```

### 2. Set Environment Variables in Vercel Dashboard
- Go to Vercel Project Settings → Environment Variables
- Add: `REACT_APP_API_URL` = `https://seatallocation.onrender.com` (your Render backend URL)

### 3. Deploy Backend First to Render
- Push to GitHub main branch
- Connect Render to your GitHub repo
- Deploy `seatallocation/` folder as Web Service

### 4. Update Frontend API URL
Once backend is deployed on Render, update the Vercel environment variable:
- `REACT_APP_API_URL` = `https://<your-render-app>.onrender.com`

## Note
The frontend files are pure HTML/CSS/JavaScript - no build process needed. 
Vercel will serve them as static files with proper routing (SPA support).

## API Endpoints Expected
- POST `/api/auth/login`
- GET `/api/auth/validate`
- POST `/api/admin/**`
- GET `/api/students/**`
- POST `/api/seating/**`
- And other REST endpoints from Spring Boot backend
