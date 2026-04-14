const baseUrl = "";
let adminToken = localStorage.getItem("adminToken") || "";
let tokenExpiry = localStorage.getItem("adminTokenExpiry") || "";

const addStudentBtn = document.getElementById("addStudentBtn");
const addHallBtn = document.getElementById("addHallBtn");
const logoutBtn = document.getElementById("logoutBtn");
const themeToggleBtn = document.getElementById("themeToggleBtn");
const adminStatus = document.getElementById("adminStatus");

addStudentBtn.addEventListener("click", addStudent);
addHallBtn.addEventListener("click", addHall);
logoutBtn.addEventListener("click", logout);

// Theme toggle functionality
if (themeToggleBtn) {
  themeToggleBtn.addEventListener("click", toggleTheme);
  updateThemeButton();
}

if (!adminToken) {
  redirectToLogin("Admin login required. Redirecting to login page.");
} else {
  validateToken();
}

function setStatus(message) {
  adminStatus.textContent = message;
}

function redirectToLogin(message) {
  adminStatus.textContent = message;
  setTimeout(() => {
    window.location.href = "index.html";
  }, 900);
}

async function validateToken() {
  if (tokenExpiry && new Date(tokenExpiry) < new Date()) {
    logout();
    return;
  }

  const response = await fetch(`${baseUrl}/api/auth/validate`, {
    headers: { "X-Admin-Token": adminToken }
  });

  if (!response.ok) {
    logout();
    return;
  }

  const data = await response.json();
  if (data.expiresAt) {
    localStorage.setItem("adminTokenExpiry", data.expiresAt);
    tokenExpiry = data.expiresAt;
  }
}

async function addStudent() {
  const payload = {
    rollNo: document.getElementById("studentRoll").value.trim(),
    name: document.getElementById("studentName").value.trim(),
    department: document.getElementById("studentDept").value.trim(),
    subject: document.getElementById("studentSubject").value.trim()
  };

  await fetch(`${baseUrl}/api/students`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  setStatus("Student added.");
}

async function addHall() {
  const payload = {
    hallNumber: document.getElementById("hallNumber").value.trim(),
    capacity: Number(document.getElementById("hallCapacity").value)
  };

  await fetch(`${baseUrl}/api/halls`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  setStatus("Exam hall added. Redirecting to dashboards...");
  window.location.href = "dashboards.html";
}

function logout() {
  localStorage.removeItem("adminToken");
  localStorage.removeItem("adminTokenExpiry");
  adminToken = "";
  redirectToLogin("Session expired. Please login again.");
}

function toggleTheme() {
  const currentTheme = localStorage.getItem('theme') || 'dark';
  const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
  
  localStorage.setItem('theme', newTheme);
  document.documentElement.setAttribute('data-theme', newTheme);
  updateThemeButton();
}

function updateThemeButton() {
  const currentTheme = localStorage.getItem('theme') || 'dark';
  const emoji = currentTheme === 'dark' ? '🌙 Dark' : '☀️ Light';
  if (themeToggleBtn) {
    themeToggleBtn.textContent = emoji;
  }
}

