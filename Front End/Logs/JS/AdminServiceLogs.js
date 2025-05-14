document.addEventListener("DOMContentLoaded", function () {

    fetch(`http://localhost:8080/admin-srvc-1.0-SNAPSHOT/api/admin/logs`)
        .then(response => {
            const container = document.getElementById("adminlogsContainer");

            if (response.status === 204) {
                container.innerHTML = "<p>No logs available.</p>";
                return [];
            }

            return response.json();
        })
        .then(logs => {
            const container = document.getElementById("adminlogsContainer");

            logs.forEach(log => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <p><strong>Message:</strong> ${log}</p>
                        `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching dishes:", error);
            document.getElementById("adminlogsContainer").innerHTML = "<p>Error loading logs.</p>";
        });
});
