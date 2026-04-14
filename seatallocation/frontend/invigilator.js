const baseUrl = (window.APP_CONFIG && window.APP_CONFIG.baseUrl)
  ? window.APP_CONFIG.baseUrl
  : "";

const invigilatorTable = document.getElementById("invigilatorTable");
const printBtn = document.getElementById("printBtn");

printBtn.addEventListener("click", () => window.print());

async function loadSeating() {
  const response = await fetch(`${baseUrl}/api/seating`);
  const seating = await response.json();
  invigilatorTable.innerHTML = "";

  seating.forEach((seat) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${seat.hallNumber}</td>
      <td>${seat.seatNumber}</td>
      <td>${seat.rollNo}</td>
      <td>${seat.department}</td>
    `;
    invigilatorTable.appendChild(row);
  });
}

loadSeating();
