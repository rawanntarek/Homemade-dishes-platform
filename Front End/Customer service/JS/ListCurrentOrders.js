document.addEventListener('DOMContentLoaded', function() {
    // Get the customer name from localStorage
    const customerName = localStorage.getItem('username');  // Assuming 'username' is the key used to store the customer name

    if (!customerName) {
        // Handle case where username is not available in localStorage
        displayErrorMessage("No customer name found in localStorage.");
        return;
    }

    // Fetch and display current orders for the customer
    fetchCurrentOrders(customerName);

    // Fetch current orders from the backend
    function fetchCurrentOrders(customerName) {
        const url = `http://localhost:8083/customer-service/api/customers/getCurrentOrders?customerName=${customerName}`;

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.length === 0) {
                    displayNoOrdersMessage();
                } else {
                    displayOrders(data);
                }
            })
            .catch(error => {
                console.error('Error fetching orders:', error);
                displayErrorMessage();
            });
    }

    function displayOrders(orders) {
        const container = document.getElementById('currentOrdersContainer');
        container.innerHTML = ''; // Clear the container before adding new content

        orders.forEach(order => {
            const orderDiv = document.createElement('div');
            orderDiv.classList.add('order-card'); // Add a class to style the card

            orderDiv.innerHTML = `
                <div class="card">
                    <h3>Order ID: ${order.orderId}</h3>
                    <p><strong>Customer:</strong> ${order.customerName}</p>
                    <p><strong>Status:</strong> ${order.status}</p>
                    <h4>Ordered Dishes:</h4>
                    <ul>
                        ${order.dishes.map(dish => `
                            <li><strong>${dish.dishName}</strong> - ${dish.amount} x $${dish.price}</li>
                        `).join('')}
                    </ul>
                    
                </div>
            `;

            container.appendChild(orderDiv);
        });
    }

    function displayNoOrdersMessage() {
        const container = document.getElementById('currentOrdersContainer');
        container.innerHTML = '<p>No current orders found.</p>';
    }

    function displayErrorMessage(message = "Error loading current orders. Please try again later.") {
        const container = document.getElementById('currentOrdersContainer');
        container.innerHTML = `<p>${message}</p>`;
    }
});
