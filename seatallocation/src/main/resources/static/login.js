const baseUrl = "";

const loginBtn = document.getElementById("loginBtn");
const loginForm = document.getElementById("loginForm");
const loginStatus = document.getElementById("loginStatus");
const resetToggle = document.getElementById("resetToggle");
const resetPanel = document.getElementById("resetPanel");
const resetBtn = document.getElementById("resetBtn");
const resetStatus = document.getElementById("resetStatus");
const themeToggleBtn = document.getElementById("themeToggleBtn");

loginBtn.addEventListener("click", login);
loginForm.addEventListener("submit", (event) => {
  event.preventDefault();
  login();
});
resetToggle.addEventListener("click", toggleReset);
resetBtn.addEventListener("click", resetPassword);

// Theme toggle functionality
if (themeToggleBtn) {
  themeToggleBtn.addEventListener("click", toggleTheme);
  updateThemeButton();
}

function setStatus(message) {
  loginStatus.textContent = message;
}

function setResetStatus(message) {
  resetStatus.textContent = message;
}

function toggleReset() {
  resetPanel.classList.toggle("d-none");
}

async function login() {
  const username = document.getElementById("loginUsername").value.trim();
  const password = document.getElementById("loginPassword").value.trim();
  loginBtn.disabled = true;

  try {
    const response = await fetch(`${baseUrl}/api/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
      setStatus("Login failed. Check credentials.");
      return;
    }

    const data = await response.json();
    if (!data.token) {
      setStatus("Login failed. Token not received.");
      return;
    }

    localStorage.setItem("adminToken", data.token);
    if (data.expiresAt) {
      localStorage.setItem("adminTokenExpiry", data.expiresAt);
    }
    setStatus("Login successful. Redirecting...");
    window.location.assign("manage.html");
  } catch (error) {
    setStatus("Login failed. Backend not reachable.");
  } finally {
    loginBtn.disabled = false;
  }
}

async function resetPassword() {
  const username = document.getElementById("resetUsername").value.trim();
  const currentPassword = document.getElementById("resetCurrentPassword").value.trim();
  const newPassword = document.getElementById("resetNewPassword").value.trim();
  const confirmPassword = document.getElementById("resetConfirmPassword").value.trim();

  if (!username || !currentPassword || !newPassword || !confirmPassword) {
    setResetStatus("Please fill out all fields.");
    return;
  }

  if (newPassword !== confirmPassword) {
    setResetStatus("New password and confirmation do not match.");
    return;
  }

  resetBtn.disabled = true;

  try {
    const response = await fetch(`${baseUrl}/api/auth/reset-password-public`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username,
        currentPassword,
        newPassword
      })
    });

    if (!response.ok) {
      setResetStatus("Password reset failed. Check your current password.");
      return;
    }

    setResetStatus("Password updated. Please login with the new password.");
  } catch (error) {
    setResetStatus("Reset failed. Backend not reachable.");
  } finally {
    resetBtn.disabled = false;
  }
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
