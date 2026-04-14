const baseUrl = (window.APP_CONFIG && window.APP_CONFIG.baseUrl)
  ? window.APP_CONFIG.baseUrl
  : "";
let adminToken = localStorage.getItem("adminToken") || "";
let tokenExpiry = localStorage.getItem("adminTokenExpiry") || "";

const addStudentBtn = document.getElementById("addStudentBtn");
const addHallBtn = document.getElementById("addHallBtn");
const allocateBtn = document.getElementById("allocateBtn");
const viewSeatingBtn = document.getElementById("viewSeatingBtn");
const logoutBtn = document.getElementById("logoutBtn");
const adminStatus = document.getElementById("adminStatus");

addStudentBtn.addEventListener("click", addStudent);
addHallBtn.addEventListener("click", addHall);
allocateBtn.addEventListener("click", allocateSeating);
viewSeatingBtn.addEventListener("click", () => {
  window.location.href = "allocation.html";
});
logoutBtn.addEventListener("click", logout);

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

  await loadStudents();
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

  await loadHalls();
}

async function allocateSeating() {
  const response = await fetch(`${baseUrl}/api/seating/allocate`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-Admin-Token": adminToken
    }
  });

  if (!response.ok) {
    if (response.status === 401) {
      logout();
      return;
    }
    setStatus("Allocation failed. Ensure hall capacity is sufficient.");
    return;
  }

  setStatus("Allocation completed.");
  window.location.href = "allocation.html";
}


async function loadStudents() {
  const response = await fetch(`${baseUrl}/api/students`);
  const students = await response.json();
  const table = document.getElementById("studentsTable");
  table.innerHTML = "";

  students.forEach((student) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${student.rollNo}</td>
      <td>${student.name}</td>
      <td>${student.department}</td>
      <td>${student.subject}</td>
    `;
    table.appendChild(row);
  });
}

async function loadHalls() {
  const response = await fetch(`${baseUrl}/api/halls`);
  const halls = await response.json();
  const table = document.getElementById("hallsTable");
  table.innerHTML = "";

  halls.forEach((hall) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${hall.hallNumber}</td>
      <td>${hall.capacity}</td>
    `;
    table.appendChild(row);
  });
}

function logout() {
  localStorage.removeItem("adminToken");
  localStorage.removeItem("adminTokenExpiry");
  adminToken = "";
  redirectToLogin("Session expired. Please login again.");
}

loadStudents();
loadHalls();
