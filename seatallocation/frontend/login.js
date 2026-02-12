const baseUrl = "https://classified-trackbacks-mel-facts.trycloudflare.com";

const loginBtn = document.getElementById("loginBtn");
const loginStatus = document.getElementById("loginStatus");
const resetToggle = document.getElementById("resetToggle");
const resetPanel = document.getElementById("resetPanel");
const resetSubmit = document.getElementById("resetSubmit");
const resetCurrentPassword = document.getElementById("resetCurrentPassword");
const resetNewPassword = document.getElementById("resetNewPassword");
const resetConfirmPassword = document.getElementById("resetConfirmPassword");

loginBtn.addEventListener("click", login);
resetToggle.addEventListener("click", toggleResetPanel);
resetSubmit.addEventListener("click", resetPassword);

function setStatus(message) {
  loginStatus.textContent = message;
}

function toggleResetPanel() {
  const isHidden = resetPanel.getAttribute("aria-hidden") !== "false";
  resetPanel.setAttribute("aria-hidden", isHidden ? "false" : "true");
  resetPanel.classList.toggle("reset-panel--open", isHidden);
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
    window.location.assign("admin.html");
  } catch (error) {
    setStatus("Login failed. Backend not reachable.");
  } finally {
    loginBtn.disabled = false;
  }
}

async function resetPassword() {
  const username = document.getElementById("loginUsername").value.trim();
  const currentPassword = resetCurrentPassword.value.trim();
  const newPassword = resetNewPassword.value.trim();
  const confirmPassword = resetConfirmPassword.value.trim();

  if (!username || !currentPassword || !newPassword) {
    setStatus("Enter user name, current password, and new password.");
    return;
  }

  if (newPassword !== confirmPassword) {
    setStatus("New password and confirmation do not match.");
    return;
  }

  resetSubmit.disabled = true;

  try {
    const response = await fetch(`${baseUrl}/api/auth/reset-password-public`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, currentPassword, newPassword })
    });

    if (!response.ok) {
      setStatus("Password reset failed. Check current password.");
      return;
    }

    resetCurrentPassword.value = "";
    resetNewPassword.value = "";
    resetConfirmPassword.value = "";
    setStatus("Password updated. Please log in.");
  } catch (error) {
    setStatus("Password reset failed. Backend not reachable.");
  } finally {
    resetSubmit.disabled = false;
  }
}
