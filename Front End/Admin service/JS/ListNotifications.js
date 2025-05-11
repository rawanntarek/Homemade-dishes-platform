document.addEventListener("DOMContentLoaded", function () {
    fetch("http://localhost:8080/admin-srvc-1.0-SNAPSHOT/api/admin/notifications")
        .then(response => {
            const container = document.getElementById("notificationContainer");

            if (response.status === 204) {
                container.innerHTML = "<p>No notifications found.</p>";
                return [];
            }

            return response.json();
        })
        .then(notifications => {
            const container = document.getElementById("notificationContainer");

            notifications.forEach(notification => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <p><strong>Order ID:</strong> ${notification.orderId}</p>
                    <p><strong>Customer Name:</strong> ${notification.customerName}</p>
                    <p><strong>Message:</strong> ${notification.message}</p>
                    <p><strong>Order Status:</strong> ${notification.status}</p>

                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching customers:", error);
            document.getElementById("customersContainer").innerHTML = "<p>Error loading customers.</p>";
        });
});
