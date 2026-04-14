const baseUrl = "";

const allocationTable = document.getElementById("allocationTable");
const printBtn = document.getElementById("printBtn");
const themeToggleBtn = document.getElementById("themeToggleBtn");

printBtn.addEventListener("click", () => window.print());

// Theme toggle functionality
if (themeToggleBtn) {
  themeToggleBtn.addEventListener("click", toggleTheme);
  updateThemeButton();
}

async function loadSeating() {
  const response = await fetch(`${baseUrl}/api/seating`);
  const seating = await response.json();
  allocationTable.innerHTML = "";

  seating.forEach((seat) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${seat.hallNumber}</td>
      <td>${seat.seatNumber}</td>
      <td>${seat.rollNo}</td>
      <td>${seat.department}</td>
    `;
    allocationTable.appendChild(row);
  });
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

loadSeating();
