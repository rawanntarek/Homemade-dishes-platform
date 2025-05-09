document.addEventListener("DOMContentLoaded", function () {
    const companyName = localStorage.getItem('username');  // Get customer name from local storage

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
                    <p><strong>Company Name:</strong> ${dish.companyName}</p>
                    <p><strong>Dish Name:</strong> ${dish.name}</p>
                    <p><strong>Price:</strong> $${dish.price}</p>
                    <input type="number" id="quantity-${dish.name}" min="1" value="1">
                    <button onclick="addToCart('${dish.name}', ${dish.price}, '${dish.companyName}')">Add to Cart</button>
                `;
                container.appendChild(card);
            });
        })
       .catch(error => console.error("Error fetching dishes:", error));

    window.cart = [];
});

function addToCart(dishName, price, companyName) {
    const quantity = document.getElementById(`quantity-${dishName}`).value;
    const existingItem = window.cart.find(item => item.dishName === dishName);

    if (existingItem) {
        existingItem.quantity += parseInt(quantity);
    } else {
        window.cart.push({ dishName, price, quantity: parseInt(quantity), companyName });
    }

    updateCart();
}

function updateCart() {
    const cartContainer = document.getElementById("cartList");
    cartContainer.innerHTML = '';  // Clear existing cart items

    let totalAmount = 0;

    window.cart.forEach(item => {
        const cartItem = document.createElement("li");
        const itemTotalPrice = item.price * item.quantity;
        totalAmount += itemTotalPrice;

        cartItem.textContent = `${item.dishName} - Quantity: ${item.quantity} - Price: $${itemTotalPrice}`;
        cartContainer.appendChild(cartItem);
    });

    const totalElement = document.createElement("p");
    totalElement.textContent = `Total: $${totalAmount}`;
    cartContainer.appendChild(totalElement);
}

function placeOrder() {
    const customerName = localStorage.getItem("username");

    const order = {
        customerName: customerName,  // Use the customer name
        status: "pending",  // Default status is "pending"
        dishes: window.cart.map(item => ({
            dishName: item.dishName,
            amount: item.quantity,
            price: item.price,
            companyName: item.companyName
        }))
    };

    fetch('http://localhost:8083/customer-service/api/customers/PlaceOrder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(order) 
    })
    .then(response => {
        if (response.ok) {
            alert('Order placed successfully!');
            window.cart = [];
            updateCart();
        } else {
            alert('Error placing order!');
        }
    })
    .catch(error => {
        alert('Error placing order!');
        console.error('Error:', error);
    });
}
