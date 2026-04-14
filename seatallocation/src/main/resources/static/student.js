const baseUrl = "";

const lookupBtn = document.getElementById("lookupBtn");
const lookupStatus = document.getElementById("lookupStatus");
const lookupTable = document.getElementById("lookupTable");
const themeToggleBtn = document.getElementById("themeToggleBtn");
const chatInput = document.getElementById("chatInput");
const chatSendBtn = document.getElementById("chatSendBtn");
const chatBox = document.getElementById("chatBox");
const chatStatus = document.getElementById("chatStatus");

lookupBtn.addEventListener("click", lookupSeat);
chatSendBtn.addEventListener("click", sendChatMessage);
chatInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter") {
    sendChatMessage();
  }
});

// Theme toggle functionality
if (themeToggleBtn) {
  themeToggleBtn.addEventListener("click", toggleTheme);
  updateThemeButton();
}

function setStatus(message) {
  lookupStatus.textContent = message;
}

async function lookupSeat() {
  const rollNo = document.getElementById("lookupRoll").value.trim();
  if (!rollNo) {
    setStatus("Please enter a roll number.");
    document.getElementById("seatDashboard").style.display = "none";
    return;
  }

  lookupTable.innerHTML = "";

  const response = await fetch(`${baseUrl}/api/seating/lookup?rollNo=${encodeURIComponent(rollNo)}`);
  if (!response.ok) {
    setStatus("No allocation found.");
    document.getElementById("seatDashboard").style.display = "none";
    addChatMessage("No seat allocation found for roll number " + rollNo + ". Please contact the administrator.", "bot");
    return;
  }

  const match = await response.json();

  setStatus("Allocation found.");
  
  // Update dashboard
  document.getElementById("dashHall").textContent = match.hallNumber;
  document.getElementById("dashSeat").textContent = match.seatNumber;
  document.getElementById("dashRoll").textContent = match.rollNo;
  document.getElementById("seatDashboard").style.display = "block";
  
  // Update table
  const row = document.createElement("tr");
  row.innerHTML = `
    <td>${match.hallNumber}</td>
    <td>${match.seatNumber}</td>
    <td>${match.rollNo}</td>
    <td>${match.department}</td>
  `;
  lookupTable.appendChild(row);
  
  // Show allocation directly in chat
  addChatMessage(`You're allocated to Hall ${match.hallNumber}, Seat ${match.seatNumber}`, "bot");
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

// Chatbot functionality
async function sendChatMessage() {
  const message = chatInput.value.trim();
  if (!message) return;

  // Add user message to chat
  addChatMessage(message, "user");
  chatInput.value = "";

  // Show loading state
  chatSendBtn.disabled = true;
  chatStatus.textContent = "AI is thinking...";

  try {
    // Get user's roll number for context if they entered one
    const userRollNo = document.getElementById("lookupRoll").value.trim();
    
    const response = await fetch(`${baseUrl}/api/chat/ask`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: message,
        rollNo: userRollNo || null
      })
    });

    if (!response.ok) {
      addChatMessage("Sorry, I couldn't process your request. Please try again.", "bot");
      chatStatus.textContent = "Error occurred";
      return;
    }

    const data = await response.json();
    addChatMessage(data.reply || "I didn't understand that. Can you rephrase?", "bot");
    chatStatus.textContent = "";
  } catch (error) {
    addChatMessage("Sorry, I'm unable to connect right now. Please try again later.", "bot");
    chatStatus.textContent = "Connection error";
  } finally {
    chatSendBtn.disabled = false;
  }
}

function addChatMessage(message, sender) {
  const messageDiv = document.createElement("div");
  messageDiv.className = `chat-message ${sender}-message`;
  
  // First escape HTML to prevent XSS
  let escaped = escapeHtml(message);
  
  // Then apply formatting: bold text (**text**) and line breaks (\n)
  const formattedMessage = escaped
    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br>');
  
  messageDiv.innerHTML = `<p>${formattedMessage}</p>`;
  chatBox.appendChild(messageDiv);
  
  // Auto-scroll to bottom
  chatBox.scrollTop = chatBox.scrollHeight;
}

function escapeHtml(unsafe) {
  return unsafe
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}
