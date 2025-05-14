document.addEventListener("DOMContentLoaded", function () {

    fetch(`http://localhost:8083/customer-service/api/customers/getAllDishes`)
        .then(response => {
            const container = document.getElementById("customerlogsContainer");

            if (response.status === 204) {
                container.innerHTML = "<p>No logs available.</p>";
                return [];
            }

            return response.json();
        })
        .then(logs => {
            const container = document.getElementById("customerlogsContainer");

            logs.forEach(log => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <p><strong>Message:</strong> ${JSON.stringify(log)}</p>
                        `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching dishes:", error);
            document.getElementById("customerlogsContainer").innerHTML = "<p>Error loading logs.</p>";
        });
});
