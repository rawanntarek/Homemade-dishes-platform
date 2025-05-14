document.addEventListener('DOMContentLoaded', function() {
    // Get the customer name from localStorage
    const customerName = localStorage.getItem('username');

    if (!customerName) {
        displayErrorMessage("Please log in to view your past orders.");
        return;
    }

    // Fetch and display past orders for the customer
    fetchPastOrders(customerName);
});

function fetchPastOrders(customerName) {
    const url = `http://localhost:8083/customer-service/api/customers/getPastOrders?customerName=${customerName}`;

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
    const container = document.getElementById('pastOrdersContainer');
    container.innerHTML = ''; // Clear the container before adding new content

    orders.forEach(order => {
        const orderDiv = document.createElement('div');
        orderDiv.classList.add('card');

        orderDiv.innerHTML = `
            <h3>Order ID: ${order.orderId}</h3>
            <p><strong>Customer:</strong> ${order.customerName}</p>
            <p><strong>Status:</strong> ${order.status}</p>
            <h4>Ordered Dishes:</h4>
            <ul>
                ${order.dishes.map(dish => `
                    <li><strong>${dish.dishName}</strong> - ${dish.amount} x $${dish.price}</li>
                `).join('')}
            </ul>
        `;

        container.appendChild(orderDiv);
    });
}

function displayNoOrdersMessage() {
    const container = document.getElementById('pastOrdersContainer');
    container.innerHTML = '<p>No past orders found.</p>';
}

function displayErrorMessage(message = "Error loading past orders. Please try again later.") {
    const container = document.getElementById('pastOrdersContainer');
    container.innerHTML = `<p>${message}</p>`;
} 