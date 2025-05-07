document.addEventListener("DOMContentLoaded", function () {
    fetch("http://localhost:8080/admin-srvc-1.0-SNAPSHOT/api/admin/getSellers")
        .then(response => {
            if (response.status === 204) {
                document.getElementById("sellersContainer").innerHTML = "<p>No sellers found.</p>";
                return [];
            }
            return response.json();
        })
        .then(sellers => {
            const container = document.getElementById("sellersContainer");
            sellers.forEach(seller => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <p><strong>Company Name:</strong> ${seller.companyName}</p>

                    <p><strong>Password:</strong> ${seller.companyPassword}</p>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching sellers:", error);
            document.getElementById("sellersContainer").innerHTML = "<p>Error loading sellers.</p>";
        });
});
