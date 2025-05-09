document.addEventListener("DOMContentLoaded", function () {
    const companyName = localStorage.getItem('username');  // Get company name from local storage

    if (!companyName) {
        document.getElementById('dishesContainer').innerHTML = '<p>Please log in first.</p>';
        return;
    }

    fetch(`http://localhost:8083/customer-service/api/customers/getAllDishes`)
        .then(response => {
            const container = document.getElementById("dishesContainer");

            if (response.status !== 200) {
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
                                    <p><strong>company Name:</strong> ${dish.companyName}</p>

                    <p><strong>Dish Name:</strong> ${dish.name}</p>
                    <p><strong>Price:</strong> $${dish.price}</p>
                     <input type="number" id="quantity-${dish.name}" min="1" value="1">
                        <button onclick="addToOrder('${dish.name}', ${dish.price})">Add to Order</button>
               
                `;
                container.appendChild(card);
            });
        })
       .catch(error => console.error("Error fetching dishes:", error));
       
        
});
