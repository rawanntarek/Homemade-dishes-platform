document.addEventListener("DOMContentLoaded", function () {
    fetch("http://localhost:8080/admin-srvc-1.0-SNAPSHOT/api/admin/getCustomers")
        .then(response => {
            const container = document.getElementById("customersContainer");

            if (response.status === 204) {
                container.innerHTML = "<p>No customers found.</p>";
                return [];
            }

            return response.json();
        })
        .then(customers => {
            const container = document.getElementById("customersContainer");

            customers.forEach(customer => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <p><strong>Customer Name:</strong> ${customer.username}</p>
                    <p><strong>Password:</strong> ${customer.password}</p>
                    <p><strong>balance: </strong> ${customer.balance}</p>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching customers:", error);
            document.getElementById("customersContainer").innerHTML = "<p>Error loading customers.</p>";
        });
});
