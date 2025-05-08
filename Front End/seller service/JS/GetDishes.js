document.addEventListener("DOMContentLoaded", function () {
    const companyName = localStorage.getItem('companyName');  // Get company name from local storage

    if (!companyName) {
        document.getElementById('dishesContainer').innerHTML = '<p>Please log in first.</p>';
        return;
    }

    fetch(`http://localhost:8082/seller-service/api/sellers/getDishes?companyName=${companyName}`)
        .then(response => {
            const container = document.getElementById("dishesContainer");

            if (response.status === 204) {
                container.innerHTML = "<p>No dishes available.</p>";
                return [];
            }

            return response.json();
        })
        .then(dishes => {
            const container = document.getElementById("dishesContainer");

            dishes.forEach(dish => {
                const card = document.createElement("div");
                card.className = "card";
                card.innerHTML = `
                    <p><strong>Dish Name:</strong> ${dish.name}</p>
                    <p><strong>Price:</strong> $${dish.price}</p>
                    <p><strong>Amount:</strong> ${dish.amount}</p>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error("Error fetching dishes:", error);
            document.getElementById("dishesContainer").innerHTML = "<p>Error loading dishes.</p>";
        });
});
