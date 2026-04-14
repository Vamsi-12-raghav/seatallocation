const baseUrl = (window.APP_CONFIG && window.APP_CONFIG.baseUrl)
  ? window.APP_CONFIG.baseUrl
  : "";

const lookupBtn = document.getElementById("lookupBtn");
const lookupStatus = document.getElementById("lookupStatus");
const lookupTable = document.getElementById("lookupTable");

lookupBtn.addEventListener("click", lookupSeat);

function setStatus(message) {
  lookupStatus.textContent = message;
}

async function lookupSeat() {
  const rollNo = document.getElementById("lookupRoll").value.trim();
  if (!rollNo) {
    setStatus("Please enter a roll number.");
    return;
  }

  lookupTable.innerHTML = "";

  const response = await fetch(`${baseUrl}/api/seating/lookup?rollNo=${encodeURIComponent(rollNo)}`);
  if (!response.ok) {
    setStatus("No allocation found. Ask admin to run allocation.");
    return;
  }

  const match = await response.json();

  setStatus("Allocation found.");
  const row = document.createElement("tr");
  row.innerHTML = `
    <td>${match.hallNumber}</td>
    <td>${match.seatNumber}</td>
    <td>${match.rollNo}</td>
    <td>${match.department}</td>
  `;
  lookupTable.appendChild(row);
}
