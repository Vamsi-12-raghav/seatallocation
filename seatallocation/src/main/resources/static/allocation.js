const baseUrl = "";

const allocationTable = document.getElementById("allocationTable");
const printBtn = document.getElementById("printBtn");

printBtn.addEventListener("click", () => window.print());

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

loadSeating();
